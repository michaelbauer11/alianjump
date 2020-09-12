from django.db import models
from django.db.models import Max
from django.contrib.postgres.fields import ArrayField

class UploadTo:
  def __init__(self, field):
    self.field = field

  def __call__(self, instance, filename):
    return '{}/{}/{}'.format(instance.reference, self.field, filename)

  def deconstruct(self):
    return ('forms.models.UploadTo', [self.field], {})

class Form(models.Model):
    reference = models.IntegerField(blank=False, default=0)
    eventType = models.CharField(max_length=70, blank=False, default='')
    date = models.CharField(max_length=70, blank=False, default='')
    reporterName = models.CharField(max_length=70, blank=False, default='')
    reporterUnit = models.CharField(max_length=70, blank=False, default='')
    editStateBlocked = models.BooleanField(default=False)

    class Meta:
      abstract = True



class EventForm(Form):
    caseIdOnMetzah = models.CharField(max_length=70, blank=False, default='')
    handlingResults = models.CharField(max_length=70, blank=False, default='')
    eventStatus = models.CharField(max_length=70, blank=False, default='')
    handlingStatus = models.CharField(max_length=70, blank=False, default='')

    class Meta:
      abstract = True

class FormsTable(EventForm):
    equipment = models.CharField(max_length=70, blank=False, default='')
    equipmentType = models.CharField(max_length=70, blank=False, default='')
    equipmentMark = models.CharField(max_length=70, blank=False, default='')
    equipmentMakat = models.CharField(max_length=70, blank=False, default='')
    
    # Events Form
    signerUnit = models.CharField(max_length=70, blank=False, default='')
    signerName = models.CharField(max_length=70, blank=False, default='')
    signerId = models.CharField(max_length=70, blank=False, default='')
    rank = models.CharField(max_length=70, blank=False, default='')
    position = models.CharField(max_length=70, blank=False, default='')
    eventDate = models.CharField(max_length=70, blank=False, default='')
    eventHour = models.CharField(max_length=70, blank=False, default='')
    eventRelevantPlacesAndFactors = models.CharField(max_length=70, blank=False, default='')
    eventInitialDetails = models.CharField(max_length=70, blank=False, default='')
    investigationDate = models.CharField(max_length=70, blank=False, default='')
    investigationFile = models.FileField(upload_to=UploadTo('investigationFile'), max_length=100, blank=True, null=True)
    handlingDate = models.CharField(max_length=70, blank=False, default='')
    findingDate = models.CharField(max_length=70, blank=False, default='')
    findingFile = models.FileField(upload_to=UploadTo('findingFile'), max_length=100, blank=True, null=True)
    handlingFile = models.FileField(upload_to=UploadTo('handlingFile'), max_length=100, blank=True, null=True)
    
    # Reviews Form
    reviewFile = models.FileField(upload_to=UploadTo('reviewFile'), max_length=100, blank=True, null=True)
    reviewDate = models.CharField(max_length=70, blank=False, default='')
    reviewReference = models.CharField(max_length=70, blank=False, default='')
    isMatchToReport = models.BooleanField(default=False)
