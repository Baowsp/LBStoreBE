package com.group.lbstore.Controller;

import com.group.lbstore.DTO.CommentRequest;
import com.group.lbstore.DTO.CommentResponse;
import com.group.lbstore.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
        CommentResponse created = commentService.createComment(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByProduct(@PathVariable Long productId) {
        List<CommentResponse> comments = commentService.findByProductId(productId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/can-comment")
    public ResponseEntity<Boolean> canComment(@RequestParam java.util.UUID userId, @RequestParam Long productId) {
        return ResponseEntity.ok(commentService.canComment(userId, productId));
    }
}