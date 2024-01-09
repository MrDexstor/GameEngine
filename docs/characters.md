# Персонажи

> Для работы с персонажами импортируйте класс:
> `ru.gameengine.Characters.Utils.CharacterBuilder`

# Создать персонажа

Что бы создать нового персонажа вам потребуется создать новый экземпляр класса CharacterBuilder и передать ему следующие параметры:

* Путь до текстуры (Например : `"textures/character/example.png"`)
* Позицию для размещения (Например : `new BlockPos(12, 71, 1012)`)
> Для передачи позиции импортируйте: `net.minecraft.util.math.BlockPos`

* Мир в котором будет размещён персонаж (Например мы можем получить мир из объекта персонажа, вот так : `player.level`)

> В последнем примере используется объект `player` -  его необходимо определить самостоятельно.
> Объект player является экзампляром класса PlayerEntity и импортируется по пути: `net.minecraft.entity.player.PlayerEntity`

По итогу мы подучим примерно следующий код:

```java
package ru.gameengine.Scripts;
// Сторонние импорты
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;


import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


// Импорты для работы GameEngine
import ru.gameengine.Characters.Utils.CharacterBuilder;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Script {
    static PlayerEntity player;
    
    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        // Получаем объект игрока из event
        player = event.getPlayer();
        
        CharacterBuilder my_first_npc = new CharacterBuilder(textures/character/example.png", new BlockPos(12, 71, 1012), player.level);
    }
}
```
 
 
# Монолог персонажа

Вам хочется заставить вашего персонажа что то рассказывать?
Давайте его заставим это делать!

Все сообщения имеют вид : `[<chat_name>] <massange>`

Для того что бы установить `chat_name` вызовите метод `chatname`, вот так:
```java
my_npc.chatname("Персонаж");
```

Что бы мы заставили персонажа разговаривать есть 2 способа:
    1. "Нацелится и говорить"
    2. "Сказать нацеленно"

1. "Нацелится и говорить" - этот метод представляет собой предварительную привязку игрока к персонажу а затем прямую отправку сообщений

> Метод "Нацелится и говорить" реализован не полностью,по этому документация не содержит информации о работе с ним.

```comment
connectPlayer(PlayerEntity player);
say(String msg);
```

2. "Сказать нацеленно" - этот метод представояет собой передачу получателя при каждом вызове метода. Используется метод sayFrom, вот так :
3. 
```java
player = event.getPlayer();
/ Назначаем получателем сообщения инициатора ивента
my_npc.sayFrom("Привет, как дела?", player);
```












# Прочие методы документация для которых ещё не готова




setAnim(String show)
renderItem(ItemStack itemStack)
moveEntity(BlockPos vector3d, float speed)
rotate(float yaw)
getName()
remove()
setPos(double x, double y, double z)
setTexture(String texture) 
setModel(String model)
setAnimationPath(String animationPath)
stopMoveEntity()