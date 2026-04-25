package com.mohamed.servicehub.di

import com.mohamed.servicehub.data.InMemoryUserFirestoreRepository
import com.mohamed.servicehub.presentation.ServeHubViewModel
import com.mohamed.servicehub.domain.repo.AuthRepository
import com.mohamed.servicehub.data.FirebaseAuthRepository
import com.mohamed.servicehub.data.FirebaseRestaurantRepository
import com.mohamed.servicehub.domain.repo.RestaurantRepository
import com.mohamed.servicehub.domain.repo.UserFirestoreRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { Firebase.auth }
    single { Firebase.firestore }

    // Repositories
    singleOf(::InMemoryUserFirestoreRepository) bind UserFirestoreRepository::class
    singleOf(::FirebaseAuthRepository) bind AuthRepository::class
    singleOf(::FirebaseRestaurantRepository) bind RestaurantRepository::class

    // ViewModel
    viewModelOf(::ServeHubViewModel)
}
