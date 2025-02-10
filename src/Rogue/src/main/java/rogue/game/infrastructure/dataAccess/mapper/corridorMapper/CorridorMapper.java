package rogue.game.infrastructure.dataAccess.mapper.corridorMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import rogue.game.domain.entities.level.Corridor;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.PositionModel;
import rogue.game.infrastructure.dataAccess.model.level.CorridorModel;

import java.util.List;

/**
 * Mapper interface for converting between {@link Corridor} and {@link CorridorModel} objects.
 *
 * <p> Uses {@link org.mapstruct.Mapper} library to generate mapping code.
 */
 @Mapper(uses = PositionMapper.class)
public interface CorridorMapper {

    @Mapping(target = "points", source = "points", qualifiedByName = "positionListToPositionModelList")
    CorridorModel toModel(Corridor corridor);

    /**
     * Converts a {@link CorridorModel} DTO to a {@link Corridor} entity.
     *
     * <p> This method maps the properties of the {@link CorridorModel} DTO to the corresponding fields of the {@link Corridor} entity.
     * <p> It handles the mapping of the 'points' list, ensuring that the list is not null before adding points to the {@link Corridor} entity.
     *
     * @param corridorModel The {@link CorridorModel} DTO to be converted.
     * @return The converted {@link Corridor} entity.
     */
    default Corridor toEntity(CorridorModel corridorModel) {
        if ( corridorModel == null ) {
            return null;
        }

        PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

        Corridor corridor = new Corridor();

        corridor.setType( corridorModel.getType() );

        if ( corridor.getPoints() != null ) {
            List<PositionModel> list = corridorModel.getPoints();

            if ( list != null )
                list.forEach( positionModel -> corridor.addPoint( positionMapper.toEntity( positionModel ) ) );
        }

        return corridor;
    }

    @Named("toCorridorModelsList")
    List<CorridorModel> toCorridorModelsList(List<Corridor> list);
}
