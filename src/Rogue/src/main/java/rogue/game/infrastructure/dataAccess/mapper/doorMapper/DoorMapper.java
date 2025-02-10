package rogue.game.infrastructure.dataAccess.mapper.doorMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rogue.game.domain.entities.level.Door;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.level.DoorModel;

import java.util.List;

/**
 * Mapper interface for converting between {@link Door} and {@link DoorModel} objects.
 *
 * <p> Uses {@link org.mapstruct.Mapper} library to generate mapping code.
 */
@Mapper(uses = { PositionMapper.class } )
public interface DoorMapper {
    DoorModel toModel(Door door);
    Door toEntity(DoorModel doorModel) ;

    @Named("toDoorModelsList")
    List<DoorModel> toDoorModelsList(List<Door> list);
}
