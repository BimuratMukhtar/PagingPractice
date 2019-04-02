# PagingPractice
The Movie Db client (Android Interview task):

https://www.themoviedb.org/documentation/api

Have two screens:

1) Search screen, searches movies by given query and show results and supports pagination. Have different states: movies loaded, loading, error, not found;
2) Movie detail, show detailed information about movie with high quality image, user can mark movie as favorite, favorite movie ids saved locally (for show in next launches);

Supports portrait and landscape screen orientations with state saved.

Uses such libraries:

Google Paging library from jetpack; <br />
Koin.io for DI; <br /> 
Glide for image processing;<br />
Android architecture components: LiveData, ViewModel, Room;<br />
Retrofit for network requests;

