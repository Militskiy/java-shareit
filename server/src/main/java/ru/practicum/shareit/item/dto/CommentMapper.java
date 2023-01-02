package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.model.Comment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {
    Comment commentCreateDtoToComment(CommentCreateDto commentCreateDto);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto commentToCommentResponseDto(Comment comment);
}
