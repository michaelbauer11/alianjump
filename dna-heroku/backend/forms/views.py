from django.shortcuts import render
from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser, FileUploadParser, MultiPartParser, FormParser
from rest_framework import status
from django.db.models import Max
from django.conf import settings

from rest_framework.views import APIView

from forms.models import FormsTable
from forms.serializers import FormsSerializer
from users.utils import check_permissions, check_permissions_dec , MANAGER, EVENTS_REPORTER, EVENTS_VIEWER
from management.utils import get_inferior_units
from msgs.utils import new_event_msgs, delete_event_messages

import time
import os
import csv
from datetime import datetime

@csrf_exempt
@check_permissions_dec([MANAGER, EVENTS_REPORTER, EVENTS_VIEWER], RETURN_UNIT=True)
def forms_list(request, event_type, unit):
    '''
        Responsible to hendle requests froms main control table page.
        Relevant urls: /api/forms/*
        GET - Return all forms matching the given event type on the url.
              Return all rows from table when no url specified.
        DELETE - Delete all table content. 
    '''
    if request.method == 'GET':
        if event_type == '':
            forms = FormsTable.objects.filter(reporterUnit__in=get_inferior_units(unit))
        else:
            forms = FormsTable.objects.filter(eventType=event_type, reporterUnit__in=get_inferior_units(unit))
        
        form_serializer = FormsSerializer(forms, many=True)

        return JsonResponse(form_serializer.data, safe=False) 
    
    elif request.method == 'DELETE':
        FormsTable.objects.all().delete()
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)

@csrf_exempt
@check_permissions_dec([MANAGER, EVENTS_REPORTER, EVENTS_VIEWER])
def new_event_form(request):
    '''
        New Event Form load all its initial values from here.
        Relevant url: /api/event_forms/
    '''
    if request.method == 'GET':
        dt = datetime.today()
        payload = {'datetime': '{}/{}/{}'.format(dt.day, dt.month, dt.year)}
        return JsonResponse(payload, safe=False)
            
@csrf_exempt
@check_permissions_dec([MANAGER, EVENTS_REPORTER, EVENTS_VIEWER])
def download_file(request, path):
    '''
        Download file from given path in the request url.
    '''
    file_path = os.path.join(settings.MEDIA_ROOT, path)
    if os.path.exists(file_path):
        with open(file_path, 'rb') as fh:
            response = HttpResponse(fh.read(), content_type="application/vnd.ms-excel")
            response['Content-Disposition'] = 'inline; filename=' + os.path.basename(file_path)
            return response
    
@csrf_exempt
@check_permissions_dec([MANAGER, EVENTS_REPORTER, EVENTS_VIEWER])
def download_xl_file(request, event_type):
    '''
        Export xl file following the given url
    '''
    response = HttpResponse(content_type='text/csv')
    response['Content-Disposition'] = 'attachement;filename=\"report.csv\"'

    response.write(u'\ufeff'.encode('utf8'))
    writer = csv.writer(response)
    writer.writerow(['reference', 'eventType', 'date', 'reporterName', 'reporterUnit', 'caseIdOnMetzah', 'handlingResults', 'eventStatus', 'handlingStatus', 'equipment', 'equipmentType' ,'equipmentMark', 'equipmentMakat', 'signerUnit', 'signerName', 'signerId', 'rank', 'position', 'eventDate', 'eventHour', 'reviewReference','isMatchToReport'])

    if event_type == '':
        forms = FormsTable.objects.all()
    else:
        forms = FormsTable.objects.filter(eventType=event_type)

    for form in forms.values_list('reference', 'eventType', 'date', 'reporterName', 'reporterUnit', 'caseIdOnMetzah', 'handlingResults', 'eventStatus', 'handlingStatus', 'equipment', 'equipmentType' ,'equipmentMark', 'equipmentMakat', 'signerUnit', 'signerName', 'signerId', 'rank', 'position', 'eventDate', 'eventHour', 'reviewReference','isMatchToReport'):
        writer.writerow(form)

    return response



class NewEventFrom(APIView):

    parser_classes = (MultiPartParser, FormParser)
    
    @check_permissions_dec([MANAGER, EVENTS_REPORTER], API_VIEW=True)
    def post(self, request, *args, **kwargs):
        form_serializer = FormsSerializer(data=request.data)

        if form_serializer.is_valid():
            reference = int(FormsTable.objects.aggregate(Max('reference'))['reference__max'] or 0) + 1
            # Create instance for this event form in the messages database
            new_event_msgs(reference)
            form_serializer.save(reference=reference)
            return JsonResponse(form_serializer.data, status=status.HTTP_201_CREATED ) 
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    
    @check_permissions_dec([MANAGER, EVENTS_REPORTER], API_VIEW=True)
    def put(self, request, reference,*args, **kwargs):
        try: 
            event_form = FormsTable.objects.get(reference=reference) 
        except FormsTable.DoesNotExist: 
            return HttpResponse(status=status.HTTP_404_NOT_FOUND) 

        form_serializer = FormsSerializer(event_form, data=request.data)
        if form_serializer.is_valid():
            form_serializer.save()
            return JsonResponse(form_serializer.data, status=status.HTTP_201_CREATED ) 
        else:
            return HttpResponse(form_serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    @check_permissions_dec([MANAGER, EVENTS_REPORTER, EVENTS_VIEWER], API_VIEW=True)
    def get(self, request, reference, *args, **kwargs):
        try: 
            event_form = FormsTable.objects.get(reference=reference)
        except FormsTable.DoesNotExist: 
            return HttpResponse(status=status.HTTP_404_NOT_FOUND) 
    
        form_serializer = FormsSerializer(event_form)
        return JsonResponse(form_serializer.data)

    @check_permissions_dec([MANAGER], API_VIEW=True)
    def delete(self, request, reference, *args, **kwargs):
        try: 
            event_form = FormsTable.objects.get(reference=reference)
        except FormsTable.DoesNotExist: 
            return HttpResponse(status=status.HTTP_404_NOT_FOUND) 

        # Delete this event form instance from the messages database
        delete_event_messages(reference)
        event_form.delete()
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)