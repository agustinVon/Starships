package starships

import edu.austral.dissis.starships.vector.Vector2

interface Movable {
    fun move(vector:Vector2, rot:Double);
}