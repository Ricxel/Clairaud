import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    private var initialized = false

    init {
        loadUser()
    }

    fun loadUser() {
        if (initialized) return

        viewModelScope.launch {
            // Simula ritardo di rete
            delay(500)
            _userData.value = User(
                favPresets = emptySet(),
                uid = 123,
                token = "authToken123",
                username = "ClairaudUser",
                mail = "user@clairaud.com"
            )
            initialized = true
        }
    }
}
