package rogue.game.infrastructure.dataAccess.mapper.balancerMapper;

import org.mapstruct.Mapper;
import rogue.game.domain.entities.Balancer;
import rogue.game.infrastructure.dataAccess.model.BalancerModel;

/**
 * Mapper for converting {@link Balancer} and {@link BalancerModel} objects.
 *
 * <p> Uses {@link org.mapstruct.Mapper} library to generate mapping code.
 */
@Mapper
public interface BalancerMapper {
    BalancerModel toModel(Balancer balancer);
    Balancer toEntity(BalancerModel balancerModel);
}
