package com.appsbase.jetcode.core.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing MVI pattern
 * @param State: UI state type
 * @param Intent: User intent/action type
 * @param Effect: Side effect type
 */
abstract class BaseViewModel<State : UiState, Intent : UiIntent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    /**
     * Handle user intents
     */
    abstract fun handleIntent(intent: Intent)

    /**
     * Update UI state
     */
    protected fun updateState(newState: State) {
        _uiState.value = newState
    }

    /**
     * Send side effect
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * Get current state
     */
    protected fun currentState(): State = _uiState.value
}
