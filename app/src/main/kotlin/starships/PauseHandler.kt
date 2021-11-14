package starships

import javafx.scene.input.KeyCode

class PauseHandler() {
    var lastClick = false
    var toggle = false
    fun handleKeyPress(keySet: Set<KeyCode>): Boolean{
        var keyFound = false
        keySet.forEach { keyCode ->
            if(keyCode == KeyCode.P){
                keyFound = true
                if(!lastClick){
                    lastClick = true
                    toggle = !toggle
                }
            }
        }
        if(!keyFound){
            lastClick = false
        }
        return toggle
    }
}