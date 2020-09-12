from django.shortcuts import render
from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser, FileUploadParser, MultiPartParser, FormParser
from rest_framework import status
from django.conf import settings

from users.utils import check_permissions, check_permissions_dec , MANAGER, EVENTS_REPORTER, EVENTS_VIEWER
from management.models import UnitsTree, ConstantsFields
from management.serializers import UnitsTreeSerializer, ConstantsFieldsSerializer
from management.utils import get_inferior_units, constants_fields_array, units_array, UNITS_TREE_OBJECT_STATIC_ID, CONSTATNS_FIELDS_OBJECT_STATIC_ID

#############################################################
#                        Units tree                         #
#############################################################        
@csrf_exempt 
@check_permissions_dec([MANAGER])
def units_tree_management(request):    
    try: 
        units_tree = UnitsTree.objects.get(unitTreeId=UNITS_TREE_OBJECT_STATIC_ID) 
    except UnitsTree.DoesNotExist: 
        units_tree = None

    if request.method == 'GET':
        if not units_tree:
            return HttpResponse(status=status.HTTP_404_NOT_FOUND) 

        units_tree_serializer = UnitsTreeSerializer(units_tree)
        return JsonResponse(units_tree_serializer.data, safe=False)
    
    elif request.method == 'POST':
        data = JSONParser().parse(request)
        units_tree_serializer = UnitsTreeSerializer(data=data) if not units_tree else UnitsTreeSerializer(units_tree, data=data)
            
        if units_tree_serializer.is_valid():
            units_tree_serializer.save(unitTreeId=UNITS_TREE_OBJECT_STATIC_ID) 
            return JsonResponse(units_tree_serializer.data,safe=False)  
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST) 
            
    elif request.method == 'DELETE':
        UnitsTree.objects.all().delete()
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)
################################################################
#                     Units tree update                        #
################################################################       
@csrf_exempt 
def units_tree_register(request):
    array_units=[]
    if request.method == 'GET': 
        try: 
            units_tree = UnitsTree.objects.get(unitTreeId=UNITS_TREE_OBJECT_STATIC_ID) 
        except UnitsTree.DoesNotExist: 
            return HttpResponse(status=status.HTTP_404_NOT_FOUND) 
        units_tree_serializer = UnitsTreeSerializer(units_tree)
        array_units.append(units_tree_serializer.data)
        return HttpResponse(array_units) 

###############################################################
#                     Constants fields                        #
###############################################################
@csrf_exempt
@check_permissions_dec([MANAGER])
def constants_fields(request):
    try: 
        constants_fields = ConstantsFields.objects.get(constantFieldId=CONSTATNS_FIELDS_OBJECT_STATIC_ID) 
    except ConstantsFields.DoesNotExist: 
        constants_fields = None
    
    if request.method == 'GET':
        if not constants_fields:
            return HttpResponse(status=status.HTTP_404_NOT_FOUND)
        
        constants_fields_serializer = ConstantsFieldsSerializer(constants_fields)
        return JsonResponse(constants_fields_serializer.data, safe=False)
    
    elif request.method == 'POST': 
        data = JSONParser().parse(request) 
        print('data',data)
        constants_fields_serializer = ConstantsFieldsSerializer(data=data) if not constants_fields else ConstantsFieldsSerializer(constants_fields, data=data) 
            
        if constants_fields_serializer.is_valid():              
            constants_fields_serializer.save(constantFieldId=CONSTATNS_FIELDS_OBJECT_STATIC_ID) 
            return JsonResponse(constants_fields_serializer.data) 
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        ConstantsFields.objects.all().delete()
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)

#################################################################
#                Constanas fields and Units                     #
#################################################################


@csrf_exempt
@check_permissions_dec([MANAGER, EVENTS_REPORTER, EVENTS_VIEWER])
def constans_fields_and_units(request):
    if request.method == 'GET':
        data = constants_fields_array()
        data["units"] = units_array()
        return JsonResponse(data)
