from django.conf.urls import url 
from users import views 
 
urlpatterns = [
    url(r'^create-User/$',views.create_user),
    # get username
    url(r'^check_login/$',views.check_login),
    url(r'^get_group_permissions_List/(?P<unit>.*)/(?P<token>.*)$',views.groups_permissions_list),
    url(r'^update_permissions_user/(?P<personalnumber>[0-9a-zא-תA-Z]+)$',views.update_permissions_users),
    #url(r'^GetUsersList/$',views.Users_list),
]