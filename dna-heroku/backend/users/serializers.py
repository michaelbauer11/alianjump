from users.models import Users
from rest_framework import serializers 

class UsersSerilazers(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields=('username',
            'firstName',
            'lastName',
            'password',
            'personalNumber',
            'rank',
            'position',
            'permissions',
            'unit',)