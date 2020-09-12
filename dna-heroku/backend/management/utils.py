
from django.shortcuts import render
from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser, FileUploadParser, MultiPartParser, FormParser
from rest_framework import status
from django.conf import settings

from management.models import UnitsTree, ConstantsFields
from management.serializers import UnitsTreeSerializer, ConstantsFieldsSerializer

import re

# static value only for represting it and pull it from db
UNITS_TREE_OBJECT_STATIC_ID = '111999'
CONSTATNS_FIELDS_OBJECT_STATIC_ID = '28032018'


def constants_fields_array():
    # If no Constatnts fields Object Initiated in the database, this function will raise an exeption
    return ConstantsFieldsSerializer(ConstantsFields.objects.get(constantFieldId=CONSTATNS_FIELDS_OBJECT_STATIC_ID)).data

def units_array():
    # If no Units Tree Object Initiated in the database, this function will raise an exeption
    units_tree = UnitsTreeSerializer(UnitsTree.objects.get(unitTreeId=UNITS_TREE_OBJECT_STATIC_ID)).data
    return re.findall(r"'name': '([\w\"\' ]+)'", str(units_tree))

def get_inferior_units(unit):
    # Return an array of all units inferior to the given units

    # Get units tree from DB
    units_tree = UnitsTreeSerializer(UnitsTree.objects.get(unitTreeId=UNITS_TREE_OBJECT_STATIC_ID)).data

    # Get the sub tree inferior to the given unit
    # Extract all units by using regex
    return re.findall(r"'name': '([\w\"\' ]+)'", find_unit_node(units_tree['treeNode'][0], unit))


def find_unit_node(node, name):
    # Recursive function that returns all the units below specific units
    if node['name'] == name:
        return str(node)
    
    if len(node['children']) < 1:
        return ''

    stringed_result = ''

    for child in node['children']:
        stringed_result += find_unit_node(child, name)
    
    return stringed_result








