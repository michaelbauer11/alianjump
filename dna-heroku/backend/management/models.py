from django.contrib.postgres.fields import JSONField, ArrayField
from django.db import models

# Create your models here.
class UnitsTree(models.Model):
    # static value only for represting it and pull it from db
    unitTreeId = models.IntegerField(blank=False, default=0)
    # max tree node id in the tree
    maxTreeNodeId = models.IntegerField(blank=False, default=0)
    # the tree node itself
    treeNode = JSONField()

class ConstantsFields(models.Model):
    constantFieldId = models.CharField(max_length=20, blank=True)

    equipmentType = ArrayField(models.CharField(max_length=20,), null=True, blank=True)
    materialType = ArrayField(models.CharField(max_length=20, blank=True), null=True, blank=True)
    equipmentMakat = ArrayField(models.CharField(max_length=20, blank=True), null=True, blank=True)
    eventStatus = ArrayField(models.CharField(max_length=20, blank=True), null=True, blank=True)
    rank = ArrayField(models.CharField(max_length=20, blank=True), null=True, blank=True)
    handlingStatus = ArrayField(models.CharField(max_length=20, blank=True), null=True, blank=True)