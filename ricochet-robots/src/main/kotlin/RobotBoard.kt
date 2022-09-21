import kotlin.math.max

data class RobotBoard(
    val width: Int,
    val height: Int,
    val robots: Set<Robot>,
    val walls: Set<Wall>,
    val goal: Point
) {
    companion object {
        fun random(
            width: Int = 8,
            height: Int = 8,
            wallDensity: Double = 0.1
        ): RobotBoard {
            val squares: List<Point> = allPoints(width, height)

            val points = squares.chooseAtRandom(1 + RobotColor.values().size)

            val goal = points.first()
            val robots = points.drop(1)
                .zip(RobotColor.values())
                .map { (point, color) -> Robot(point, color) }

            val horizontalWalls = allPoints(width, height, startY = 1)
                .map { Wall(it, it + Point(1, 0)) }
            val verticalWalls = allPoints(width, height, startX = 1)
                .map { Wall(it, it + Point(0, 1)) }
            val allWalls = horizontalWalls + verticalWalls
            val numWalls = (allWalls.size * wallDensity).toInt()
            val walls = allWalls.chooseAtRandom(numWalls)

            return RobotBoard(
                width = width,
                height = height,
                goal = goal,
                robots = robots.toSet(),
                walls = walls.toSet()
            )
        }

        private tailrec fun solve(
            maxSteps: Int,
            stepsTaken: Int,
            visited: Set<RobotBoard>,
            possibilities: List<Possibility>
        ): List<Solution> {
            println("Steps: $stepsTaken, possibilities: ${possibilities.size}")
            val solutions = possibilities.filter { it.first.isSolved() }
            return when {
                solutions.isNotEmpty() -> solutions.map { it.second }
                stepsTaken >= maxSteps -> emptyList()
                possibilities.isEmpty() -> emptyList()
                else -> {
                    val nextPossibilities = possibilities.flatMap { it.next() }.filter { !visited.contains(it.first) }
                    val nextVisited = visited + nextPossibilities.map { it.first }
                    solve(maxSteps, stepsTaken + 1, nextVisited, nextPossibilities)
                }
            }
        }

        fun Possibility.next(): List<Possibility> {
            val nextBoardsAndMoves = this.first.next()

            return nextBoardsAndMoves.map { (nextBoard, nextMove) -> Possibility(nextBoard, this.second + nextMove) }
        }
    }

    private fun isSolved() = robots.any { it.position == goal }

    fun solve(maxSteps: Int = 8): List<Solution> = solve(maxSteps, 0, setOf(this), listOf(this to emptyList()))

    private fun next(): List<Pair<RobotBoard, Move>> = Move.all.map { move -> this.next(move) to move }

    private fun next(move: Move): RobotBoard {
        val (matchingRobots, otherRobots) = robots.partition { it.color == move.color }
        val robot = matchingRobots.first()
        val steps = generateSequence(robot.position) { it + move.direction.delta }
            .takeWhile {
                it.x in 0 until width &&
                        it.y in 0 until height &&
                        !otherRobots.any { robot -> robot.position == it } &&
                        !walksThroughWall(it - move.direction.delta, it)
            }

        val newRobot = Robot(steps.lastOrNull() ?: robot.position, robot.color)
        return RobotBoard(
            this.width,
            this.height,
            (otherRobots + newRobot).toSet(),
            this.walls,
            this.goal
        )
    }

    private fun walksThroughWall(from: Point, to: Point): Boolean = when {
        (from.x == to.x) -> {
            val maxY = max(from.y, to.y)
            walls.contains(Wall(Point(from.x, maxY), Point(from.x + 1, maxY)))
        }
        else -> {
            val maxX = max(from.x, to.x)
            walls.contains(Wall(Point(maxX, from.y), Point(maxX, from.y + 1)))
        }
    }

    fun draw(): String {
        return (0 until height).joinToString("\n") { y ->
            val topLine = (0 until width).joinToString("") { x ->
                (if (Point(x, y).touchesWall()) "+" else ".") +
                        (if (y == 0 || Wall(Point(x, y), Point(x + 1, y)) in walls) "-" else ".")
            } + "+"
            val bottomline = (0 until width).joinToString("") { x ->
                (if (x == 0 || Wall(Point(x, y), Point(x, y + 1)) in walls) "|" else ".") +
                        Point(x, y).character()
            } + "|"
            topLine + "\n" + bottomline
        } + "\n" + "+-".repeat(this.width) + "+"
    }

    private fun Point.touchesWall() = walls.any { this == it.start || this == it.end }
            || this.x == 0 || this.y == 0

    private fun Point.character(): Char {
        val robot = robots.firstOrNull { it.position == this }
        return when {
            robot != null -> robot.color.name.first()
            this == goal -> '*'
            else -> ' '
        }
    }
}

// We identify each square by the upper-left corner; the upper-left of the board is (0,0).
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(this.x + other.x, this.y + other.y)
    operator fun minus(other: Point) = Point(this.x - other.x, this.y - other.y)
}

fun allPoints(width: Int, height: Int, startX: Int = 0, startY: Int = 0): List<Point> =
    (startX until width).flatMap { x ->
        (startY until height).map { y ->
            Point(x, y)
        }
    }

enum class RobotColor {
    RED, BLUE, YELLOW, GREEN
}

data class Robot(val position: Point, val color: RobotColor)

data class Wall(val start: Point, val end: Point)

enum class Direction(val delta: Point) {
    UP(Point(0, -1)),
    DOWN(Point(0, +1)),
    LEFT(Point(-1, 0)),
    RIGHT(Point(+1, 0))
}

data class Move(val color: RobotColor, val direction: Direction) {
    companion object {
        val all = RobotColor.values().flatMap { color ->
            Direction.values().map { direction -> Move(color, direction) }
        }
    }
}
typealias Solution = List<Move>
typealias Possibility = Pair<RobotBoard, Solution>


fun <T> Collection<T>.chooseAtRandom(amount: Int): List<T> = this.shuffled().take(amount)

fun main() {
    val boards = generateSequence { RobotBoard.random() }
    val boardsAndSolutions = boards.map { it to it.solve(maxSteps = 6) }
    val (board, solution) = boardsAndSolutions.first {
        it.second.size == 1 &&
                it.second.first().size in 4 until 6
    }
    println(board.draw())
    println(solution)
}