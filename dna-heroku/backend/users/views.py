from django.shortcuts import render
from users.models import  Users
from users.serializers import UsersSerilazers
from rest_framework.parsers import JSONParser
from django.http import HttpResponse, JsonResponse
from rest_framework import status
from django.views.decorators.csrf import csrf_exempt
from management.utils import constants_fields_array
from management.models import UnitsTree, ConstantsFields
from users.utils import check_permissions_dec , MANAGER, EVENTS_REPORTER, EVENTS_VIEWER, check_token, create_jwt


###############################################################
#                      Create new user                        #
###############################################################
@csrf_exempt 
@check_permissions_dec([MANAGER])
def create_user(request):
    print(request)
    user_data = JSONParser().parse(request)
    user_serializer = UsersSerilazers(data=user_data)
    
    if request.method == 'POST':
        
        if(user_serializer.is_valid() and not check_user_and_personal_number(user_data)):
            user_serializer.save()
            return HttpResponse(status=status.HTTP_204_NO_CONTENT)

        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
###############################################################
#                         Login page                          #
###############################################################
@csrf_exempt 
def check_login(request):
    user_data = JSONParser().parse(request)
    if request.method == 'POST':
        try:
            user = Users.objects.get(username=user_data["username"])
        except:
            return HttpResponse(status=status.HTTP_401_UNAUTHORIZED)
            
        if check_user_password(user.username, user_data["password"]):
            user_serialized_data = UsersSerilazers(user).data
            user_serialized_data.update(access_token=create_jwt(user_data))
            return JsonResponse(user_serialized_data , safe=False)
        else:
            return HttpResponse(status=status.HTTP_401_UNAUTHORIZED)

################################################################
#                     Check propriety user                     #
################################################################

################## Check password ##############################
def check_user_password(username,password):
    return Users.objects.filter(username=username,password=password).exists()

################### Check personal number ######################
def check_user_and_personal_number(user_data):
    return (Users.objects.filter(username=user_data["username"]).exists() or Users.objects.filter(personalNumber=user_data["personalNumber"]).exists())

###############################################################
#                    Get groups permissions                   #
###############################################################
@csrf_exempt 
@check_permissions_dec([MANAGER])
def groups_permissions_list(request, unit, token):
    if request.method == 'GET':
            users = Users.objects.filter(unit=unit)
            customer_u=UsersSerilazers(users,many=True)
            return JsonResponse(customer_u.data, safe=False)     
    elif request.method == 'DELETE':
        Users.objects.all().delete()
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)
###############################################################
#                         Update user                         #
###############################################################
@csrf_exempt 
@check_permissions_dec([MANAGER])
def update_permissions_users(request, personalnumber):
    try: 
        event_form = Users.objects.get(personalNumber=personalnumber)
    except Users.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND) 
    if request.method=='GET':
            customer_serializer = UsersSerilazers(event_form)
            return JsonResponse(customer_serializer.data)
    elif request.method == 'PUT':
        form_data = JSONParser().parse(request)
        form_serializer = UsersSerilazers(event_form, data=form_data["permissions"])
        if form_serializer.is_valid():
            form_serializer.save()
            return JsonResponse({"data":form_serializer.data},status=status.HTTP_204_NO_CONTENT)