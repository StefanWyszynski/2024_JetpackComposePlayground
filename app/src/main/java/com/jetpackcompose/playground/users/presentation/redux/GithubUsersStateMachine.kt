package com.jetpackcompose.playground.users.presentation.redux

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.freeletics.flowredux.dsl.State
import com.jetpackcompose.playground.users.domain.use_case.GithubSearchUserUseCase
import com.jetpackcompose.playground.utils.NetworkOperation
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GithubUsersStateMachine @Inject constructor(
    private val githubSearchUserUseCase: GithubSearchUserUseCase,
) : FlowReduxStateMachine<GithubUserState, GithubUserAction>(
    initialState = GithubUserState.ContentState(userName = "", users = emptyList())
) {
    init {
        spec {
            // ContentState
            inState<GithubUserState.ContentState> {
                on { action: GithubUserAction.TypeUserName, state: State<GithubUserState.ContentState> ->
                    state.mutate { copy(userName = action.userName) }
                }

                on { _: GithubUserAction.Confirm, state: State<GithubUserState.ContentState> ->
                    val userName = state.snapshot.userName
                    if (userName.isNotBlank()) {
                        state.override { GithubUserState.Load(userName = userName) }
                    } else {
                        state.noChange()
                    }
                }
            }
            // Error
            inState<GithubUserState.Error> {
                on { _: GithubUserAction.RetryLoadingUserAction, state: State<GithubUserState.Error> ->
                    state.override { GithubUserState.Load(userName = state.snapshot.userName) }
                }
            }
            inState<GithubUserState.Load> {
                onEnter { state: State<GithubUserState.Load> ->
                    val userName = state.snapshot.userName
                    val searchUser = githubSearchUserUseCase(userName = userName)
                    when (searchUser) {
                        is NetworkOperation.Failure -> state.override {
                            GithubUserState.Error(Throwable("Fail"), userName = userName)
                        }

                        is NetworkOperation.Success -> state.override {
                            GithubUserState.ContentState(
                                users = searchUser.data,
                                userName = userName
                            )
                        }

                        is NetworkOperation.Loading -> state.noChange()
                    }
                }
            }
        }
    }

}