# Персонажи

> Для работы с персонажами импортируйте класс:
> `ru.gameengine.Characters.Utils.CharacterBuilder`

# Создать персонажа

Что бы создать нового персонажа вам потребуется создать новый экземпляр класса CharacterBuilder и передать ему следующие параметры:

* Путь до текстуры (Например : `"textures/character/example.png"`)
* Позицию для размещения (Например : `new BlockPos(12, 71, 1012)`)
> Для передачи позиции импортируйте: `net.minecraft.util.math.BlockPos`

* Мир в котором будет размещён персонаж (Например мы можем получить мир из объекта персонажа, вот так : `player.level`)
* 
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
        
        CharacterBuilder dex = new CharacterBuilder("textures/entity/mrdexstor.png", new BlockPos(player.getX(), player.getY()+5, player.getZ()), player.level);
    }
}
```