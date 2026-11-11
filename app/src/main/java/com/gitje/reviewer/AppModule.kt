package com.gitje.reviewer

import com.gitje.reviewer.data.ReviewDatabase
import com.gitje.reviewer.logic.GeneralRepository
import com.gitje.reviewer.logic.GeneralViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        ReviewDatabase.getDatabase(androidContext())
    }
    single { GeneralRepository(get()) }
    viewModel { GeneralViewModel(get(), get()) }
}