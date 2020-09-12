from django.conf.urls import url 
from forms import views 
 
urlpatterns = [
    # control table xl export requests
    url(r'^forms/xl/(?P<event_type>[א-ת\-\/ ]*)$', views.download_xl_file),
    # control table requests
    url(r'^forms/(?P<event_type>[א-ת\-\/ ]*)$', views.forms_list),
    # requests to load values from server - time/determined fields
    url(r'^event-forms/values$', views.new_event_form),
    # requests for a new form creation
    url(r'^event-forms/$', views.NewEventFrom.as_view()),
    # requests to a specific form in the db table
    url(r'^event-forms/(?P<reference>[0-9]+)$', views.NewEventFrom.as_view()),
    url(r'^media/(?P<path>.+)$', views.download_file),
]
