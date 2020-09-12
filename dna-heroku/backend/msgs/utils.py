
from msgs.models import Msgs

def new_event_msgs(reference):
    '''
        Create new msgs row in the database, related to a specific event form
    '''
    event_form_msgs = Msgs(reference=reference)
    event_form_msgs.save()

def delete_event_messages(reference):
    '''
        Delete specific event form msgs row in the database 
    '''
    event_form_msgs = Msgs.objects.get(reference=reference)
    event_form_msgs.delete()
