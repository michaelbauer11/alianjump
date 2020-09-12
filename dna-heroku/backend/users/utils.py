from jose import jwt
from django.http import HttpResponse
import datetime
from django.contrib.auth import authenticate
from _datetime import timedelta
from rest_framework import status
from django.http.response import JsonResponse

from users.models import  Users
from users.serializers import UsersSerilazers
from django.contrib.auth import get_user_model

MANAGER="מנהלן מערכת"
EVENTS_REPORTER="מדווח אירועים"
EVENTS_VIEWER="צופה אירועים"


secret='''PDvnOudatcLzb/i2cCVFQgIEUgTbehke5iN2QRF7Vqo2zYOzdXMuelzf5/DL+g7+
        sdXic+dLR+obFfHNwMjmaQLFRM8IAtjT2iLlIBc1amcUMx2Vy5dIlVWTA0p79bkL
        syRrY+HK8IGok4tmPAOgDIesVnWeT1i1R/7gc6jX2J84UeKM0f4wIqE/zx0oR8XI
        mZa4rpuzPmPgc+1rqFpIE8ZchdDWT+vW4vwMzQZw1Jx3sP/ubOA9rQGZoCPu4BA0
        GZ+o8wn9OFgRnOiIjJ3eVlowmz++LfqxYpFeacCbxQPh5taVQWjiPMhWRLhN2Hke
        pGD5/rjdGz2ckTM/ge9uN4Z5niX7pq02VCz5qQC9txRnefcVOLEJg5RzCrtHVVe6
        wMlYPBATeuVsPuZTPaf8ZXQblaAslSkugbKyipt0fjuflD4Ffu9eODMHCyUb8uvD
        1fZjYXAI5ydaUFwfgN/M74F12m/DMgDVS6S267j9X4T1WHgAuDY/6Gkx6iY3oXFP
        mSYBkcixaH1/+dMStDJ2OdfsmIljuhMJEedogU1SnfbQhTYLvGNiKulL6YLwwIck
        hTwkFgW7OrZo4M9tvVqL8g=='''

def create_jwt(user_data):
        expiry=""
        """
        the above token need to be saved in database, and a one-to-one
        relation should exist with the username/user_pk
        """
        username = user_data['username']
        password = user_data['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            expiry = datetime.date.today() + timedelta(days=50)
        token = jwt.encode({'username': username,'password':password}, 'secret', algorithm='HS256')
        if check_token(token):
            return token
        else:
            return False

def check_token(token):
    try:
        auth = jwt.decode(token, 'secret', algorithms=['HS256'])
        return get_permissions(auth["username"])
    except Exception as e:

        return False

def get_permissions(username):
    user = Users.objects.get(username=username)
    user_serializer = UsersSerilazers(user)
    return (user_serializer.data["permissions"], user_serializer.data["unit"])

def check_permissions_dec(permissions_array, API_VIEW=False, RETURN_UNIT=False):
    
    def wrapper(view_function):
        def functions_args(*args, **kwargs):
            # return view_function(unit='',*args, **kwargs)
            request_index = 0 if not API_VIEW else 1
            request = args[request_index]
            
            if not 'Authorization' in request.headers:
                return HttpResponse(status=status.HTTP_401_UNAUTHORIZED)
            
            token = request.headers['Authorization'].split(" ")[1]        
            permission, unit = check_token(token)
    
            if permission in permissions_array:
                return view_function(*args, **kwargs) if not RETURN_UNIT else view_function(unit=unit, *args, **kwargs)
            else:
                return HttpResponse(status=status.HTTP_401_UNAUTHORIZED)
        return functions_args
    return wrapper

def check_permissions(request, permissions_array):
    token=request.headers['Authorization']
    token=token.split(" ")
    token=token[1]
    permission=check_token(token)
    return permission in permissions_array

