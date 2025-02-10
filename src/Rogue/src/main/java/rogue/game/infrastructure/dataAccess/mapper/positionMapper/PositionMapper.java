package rogue.game.infrastructure.dataAccess.mapper.positionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rogue.game.domain.entities.Position;
import rogue.game.infrastructure.dataAccess.model.PositionModel;

import java.util.List;

/**
 * Mapper for converting {@link Position} and {@link PositionModel} objects.
 *
 * <p> Uses the {@link org.mapstruct.Mapper} library to automatically generate mapping code.
 */
@Mapper
public interface PositionMapper {
    PositionModel toModel(Position pos);
    Position toEntity(PositionModel posDto);

    @Named("positionListToPositionModelList")
    List<PositionModel> positionListToPositionModelList(List<Position> list);
}
