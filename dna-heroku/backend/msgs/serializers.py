
from rest_framework import serializers 
from msgs.models import Msgs


class MsgsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Msgs
        fields = (
            'reference',
            'messages'
        )