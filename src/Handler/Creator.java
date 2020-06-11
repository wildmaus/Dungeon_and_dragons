package Handler;

import java.awt.*;
import java.util.ArrayList;

public class Creator {
    // достаточно внести все параметры здесь и получим игру (та-да!!!)

    // ГЕРОЙ (прописываем единожды, далее лишь изменяем стартовую позицию)
    public static int cwidth = 20; // хитбокс
    public static int cheight = 20; // хитбокс
    public static double moveSpeed = 0.3;
    public static double maxSpeed = 1.6;
    public static double fallSpeed = 0.15;
    public static double maxFallSpeed = 4.0;
    public static double jumpStart = -4.8; // скорость прыжка с -
    public static int maxHealth = 5;
    public static int maxNumberOfShells = 2500; // максимум снарядов * 100 (оно же начальное число)
    public static int restorationOfShells = 1; // сколько снарядов востанавливается в секунду
    public static int shotPrice = 200; // сколько снарядов тратится за выстрел * 100
    public static int shotDamage = 5;
    public static int meleeDamage = 1;
    public static int meleeRange = 40; // расстояние атаки
    // анимации действий (предусмотренно покой, ходьба, прыжок, падение, планирование, дальняяя атака, ближняя атака)
    // именно в таком порядке
    public static String playerAnimation = "/Sprites/Player/playersprites.gif"; // путь к файлу с спрайтами, дальше идут параметры спрайтов
    public static int[] numFrames = {2, 8, 1, 2, 4, 2, 5}; // количество кадров для соответствующего действия
    public static int[] frameWidths = {30, 30, 30, 30, 30, 30, 60}; // длина спрайта для соответствующего действия
    public static int[] frameHeights = {30, 30, 30, 30, 30, 30, 30}; // высота спрайта для соответствующего действия
    public static int[] spriteDelays = {20, 3, 6, 5, 5, 5, 5}; // скорость смены кадров соответствующего действия
    // звуки
    public static String sfxJump = "/SFX/jump.mp3"; // путь к файлу с звуком прыжка
    public static String sfxShot = "/SFX/fire.mp3"; // путь к файлу с звуком дальней атаки
    public static String sfxHit = "/SFX/hit.mp3"; // путь к файлу с звуком получения урона
    public static String sfxMelee = "/SFX/scratch.mp3"; // путь к файлу с звуком ближней атаки
    public static String sfxEnemyHit = "/SFX/enemyhit.mp3"; // путь к файлу с звуком попадания по врагу


    // СНАРЯДЫ
    public static double shellMoveSpeed = 3.8;
    public static int shellCwidth = 14;
    public static int shellcheight = 14;
    // анимация
    public static String shellAnimation = "/Sprites/Player/fireball.gif"; // первая строка сам снаряд, вторая - анимация попадания
    public static int shellDelay = 6;


    // ВРАГИ

    // бегающий по оси х враг
    public static double exMoveSpeed = 0.3;
    public static double exMaxSpeed = 0.3;
    public static int exCwidth = 20; // хитбокс
    public static int exCheight = 20; // хитбокс
    public static double exFallSpeed = 0.3;
    public static double exMaxFallSpeed = 5.0;
    public static int exMaxHealth = 2;
    public static int exContactDamage = 1; // урон наносимый при соприкосновении
    // анимация движения
    public static String exAnimation = "/Sprites/Enemies/snail.gif"; // спрайты 30х30
    public static int exDelay = 20; // скорость смены кадров анимации

    // перемещающийся по оси у враг
    // аля паук, враг спускающийся на паутине сверху
    public static double eyMoveSpeed = 0.5;
    public static double eyMaxSpeed = 4.0;
    public static int eyCwidth = 25; // хитбокс
    public static int eyCheight = 25; // хитбокс
    public static int eyMaxHealth = 10;
    public static int eyContactDamage = 1; // урон наносимый при соприкосновении
    // анимация движения
    public static String eyAnimation = "/Sprites/Enemies/arachnik.gif"; // спрайты 30х30
    public static int eyDelay = 10; // скорость смены кадров анимации

    // анимация смерти врагов (EXPLOSION)
    public static String explosionAnimation = "/Sprites/Enemies/explosion.gif"; // спрайты 30х30
    public static int explosinDelay = 6; // скорость смены кадров анимации
    // звук
    public static String explosionSound = "/SFX/explode.mp3"; // путь к файлу с звуком смерти врагов


    // HUD
    public static String HUDAnimation = "/HUD/hud.gif"; // путь к файлу с худом
    // остальные настройки в классе HUD


    // ТЕЛЕПОРТ В КОНЦЕ УРОВНЯ
    public static String endLevelAnimation = "/Sprites/Player/teleport.gif"; // путь ... с телепортом обозначающим конец уроня
    public static int endLevelDelay = 4; // скорость смены кадров анимации



    // УРОВНИ (а тут мы умрем....)
    public static int NumLevels = 2;
    public static String[] TileSet = new String[] {"/Tilesets/grasstileset.gif", "/Tilesets/ruinstileset.gif"}; // путь к нужному tileset'у
    public static String[] Map = new String[] {"/Maps/level1.map", "/Maps/level2.map"}; // ... к карте
    public static String[] Bacground = new String[]{"/Backgrounds/grassbg1.gif", "/Backgrounds/menubg.gif"}; // ... к заднему фону
    public static String[] BgMusic = new String[] {"/Music/level1.mp3", "/Music/level2.mp3"}; // ... к фоновой музыке

    public static Point[][] Positions = new Point[][]{
            new Point[]{
                new Point(100, 100), // стартовая позиция игрока lvl1 x, y
                new Point(50, 200) // позиция телепорта lvl1 x, y
            },
            new Point[]{
                new Point(300, 161), // стартовая позиция игрока lvl2 x, y
                new Point(3700, 134) // позиция телепорта lvl2 x, y
            }
    };


    public static Point[][] PositionsEx = new Point[][] {
            new Point[]{
                    new Point(200, 200),
                    new Point(860, 200),
                    new Point(1525, 200),
                    new Point(1680, 200),
                    new Point(1800, 200)
            },
            new Point[]{
                    new Point(1300, 100),
                    new Point(1320, 100),
                    new Point(1340, 100),
            }
    }; // позиции врагов бегающих по х для соответствующего уровня
    public static Point[][] PositionnsEy = new Point[][] {
            new Point[]{
                    new Point(1900, 30),
                    new Point(1940, 30),
                    new Point(2000, 30),
                    new Point(2080, 30),
                    new Point(2150, 60),
                    new Point(2200, 90)
            },
            new Point[] {
            }
    }; // позиции врагов перемещающихся по y для соответствующего уровня
    public static int[][] EyAsplus = {
            {120, 120, 150, 150, 120, 120},
            {}}; // амплитуды врагов ... по у для соответствующего уровня

}
// pls kill me