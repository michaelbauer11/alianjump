from rest_framework import serializers 
from management.models import UnitsTree, ConstantsFields

class UnitsTreeSerializer(serializers.ModelSerializer):
    class Meta:
        model = UnitsTree
        fields = (
            'unitTreeId',
            'maxTreeNodeId',
            'treeNode',
        )

class ConstantsFieldsSerializer(serializers.ModelSerializer):
    class Meta:
        model = ConstantsFields
        fields = (
            'equipmentType',
            'materialType',
            'equipmentMakat',
            'eventStatus',
            'rank',
            'handlingStatus'
        )

