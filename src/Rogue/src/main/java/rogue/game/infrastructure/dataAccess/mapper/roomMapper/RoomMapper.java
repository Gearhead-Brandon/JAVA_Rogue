package rogue.game.infrastructure.dataAccess.mapper.roomMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import rogue.game.domain.entities.level.Door;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.Direction;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.doorMapper.DoorMapper;
import rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper.GameEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.level.DoorModel;
import rogue.game.infrastructure.dataAccess.model.level.RoomModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Mapper for converting {@link Room} and {@link RoomModel} objects.
 *
 * <p> Uses the {@link org.mapstruct.Mapper} library to automatically generate the mapping code.
 */
@Mapper(uses = {DoorMapper.class, PositionMapper.class, GameEntityMapper.class})
public interface RoomMapper {
    /**
     * Converts a {@link Room} entity to a {@link RoomModel} DTO.
     *
     * <p> When converting:
     * <ul
     * <li> - Room connections are converted to a {@link List} of sector numbers.</li>
     * <li> - Doors are converted to a {@link List} of {@link DoorModel}.</li>
     * <li> - Entities in the room are converted to a {@link List} of {@link GameEntityModel}.</li>
     * </ul
     *
     * @param room The room entity.
     * @return Room model.
     */
    @Mapping(target = "connections", source = "room", qualifiedByName = "toRoomConnectionsList")
    @Mapping(target = "doors", source = "doors", qualifiedByName = "toDoorModelsList")
    @Mapping(target = "entities", source = "entities", qualifiedByName = "toGameEntityModelList")
    RoomModel toModel(Room room) ;

    default Room toEntity(RoomModel roomModel){
        if ( roomModel == null ) {
            return null;
        }

        PositionMapper positionMapper = MapperRegistry.getMapper( PositionMapper.class );
        GameEntityMapper gameEntityMapper = MapperRegistry.getMapper( GameEntityMapper.class );

        Room room = new Room();

        room.setSector( roomModel.getSector() );
        room.setGrid_i( roomModel.getGrid_i() );
        room.setGrid_j( roomModel.getGrid_j() );
        room.setTopLeft( positionMapper.toEntity( roomModel.getTopLeft() ) );
        room.setBottomRight( positionMapper.toEntity( roomModel.getBottomRight() ) );

        Direction[] directions = Direction.values();

        List<DoorModel> doors = roomModel.getDoors();

        for (int i = 0; i < directions.length; i++) {
            if(doors.get(i) == null) continue;
            room.setDoorByDirection( directions[i], positionMapper.toEntity(doors.get(i).getPosition()));
            Optional<Door> optionalDoor = room.getDoorByDirection(directions[i]);

            int t = i;
            optionalDoor.ifPresent(door -> {
                door.setColor(doors.get(t).getColor());
                door.setOpen(doors.get(t).isOpen());
            });
        }

        roomModel.getEntities().forEach(gameEntityModel -> room.addEntity(gameEntityMapper.toEntity(gameEntityModel)));

        return room;
    }

    @Named("toRoomModelsList")
    List<RoomModel> toRoomModelsList(List<Room> list);

    /**
     * Maps the connections of a {@link Room} entity to a{@link List} of {@link Integer} sector numbers.
     *
     * <p> This method iterates over the {@link Direction} and retrieves the connected sector number for each {@link Direction}.
     *
     * @param r The {@link Room} entity to map.
     * @return A {@link List} of sector numbers representing the connections.
     */
    @Named("toRoomConnectionsList")
    default List<Integer> toRoomConnectionsList(Room r){
        List<Integer> sectors = Arrays.asList(null, null, null, null);

        for(Direction direction : Direction.values())
            sectors.set(direction.value, r.getConnectionRoom(direction)
                    .map(Room::getSector)
                    .orElse(null));

        return sectors;
    }
}
