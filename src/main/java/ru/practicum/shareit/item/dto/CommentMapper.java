package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {
    Comment commentCreateDtoToComment(CommentCreateDto commentCreateDto);

    List<CommentResponseDto> map(List<Comment> comments);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto commentToCommentResponseDto(Comment comment);
}
