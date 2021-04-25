package com.urani.favoriteplaces.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.urani.favoriteplaces.database.entities.Place
import com.urani.favoriteplaces.repositories.place.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject
constructor(
   private val placeRepository: PlaceRepository
) : ViewModel() {


   fun insertPlace(place: Place) {
      viewModelScope.launch {
         placeRepository.insert(place)
      }
   }

   fun updatePlace(place: Place) {
      viewModelScope.launch {
         placeRepository.update(place)
      }
   }

   fun getAllFavoritePlaces(): LiveData<List<Place>> {
      return placeRepository.getAllPlaces()
   }

   fun deleteAllPlaces() {
      viewModelScope.launch {
         placeRepository.deleteAll()
      }
   }

}


