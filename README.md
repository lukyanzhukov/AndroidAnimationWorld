# AndroidAnimationWorld
Примеры использования анимаций в jetpack compose и view, а также замеры perfomance для https://podlodka.io/droidcrew
# Что тут есть?
* Примеры анимаций Jetpack Compose от самого низкоуровневого api до Compose Motion Layout
* Примеры анимаций на классических view (Value/Object Animator, SpringAnimation, MotionLayout)
* Замеры перфоманса для анимаций на Jetpack Compose TargetBasedAnimation с помощью Jetpack Macrobenchmark
* Замеры перфоманса для анимаций на Value Animator с помощью Jetpack Macrobenchmark
# Как запустить примеры?
Открываем список *TODO* в модуле :app в Android Studio и запускаем нужные нам примеры
# Как запустить проверку перфоманса?
* Открыть файл *AnimationPerfomanceBenchmark.kt* из модуля macrobenchmark
* Для запуска тестирования perfomance jetpack compose запустить тест *checkComposeAnimation*
* Для запуска тестирования perfomance view запустить тест *checkViewAnimation*
