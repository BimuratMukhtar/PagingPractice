package com.zerotoonelabs.paginationpractice.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zerotoonelabs.paginationpractice.repository.DataRepository

class DetailViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()

    //this field is created in order to achive read only access from outside of view model
    val isFavorite: LiveData<Boolean> = Transformations.map(_isFavorite){
        it
    }

    fun getIsFavorite(movieId: Int) {
        dataRepository.getFavoriteByMovieId(movieId){
            _isFavorite.postValue(it)
        }
    }

    fun addToFavorite(movieId: Int){
        dataRepository.addToFavorite(movieId){
            if(it){
                _isFavorite.postValue(true)
            }
        }
    }

    fun removeFromFavorite(movieId: Int){
        dataRepository.deleteFromFavorite(movieId){
            if(it){
                _isFavorite.postValue(false)
            }
        }
    }


}