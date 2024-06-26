package com.jetpackcompose.playground.repos.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Parcelize
data class GithubRepo(
    var userName: String? = null,
    var avatarUrl: String? = null,
    var repoName: String? = null,
    var fullName: String? = null,
    var description: String? = null
) : Parcelable