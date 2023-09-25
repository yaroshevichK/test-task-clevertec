package task.clevertec.mapper;

public interface Mapper<TEntity, Response> {
    TEntity ResponseToEntity(Response response);

    Response entityToResponse(TEntity entity);
}
