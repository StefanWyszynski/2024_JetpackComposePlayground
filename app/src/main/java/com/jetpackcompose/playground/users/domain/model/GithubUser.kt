package com.jetpackcompose.playground.users.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@Parcelize
data class GithubUser(
    var userName: String? = null,
    var avatarUrl: String? = null
) : Parcelable