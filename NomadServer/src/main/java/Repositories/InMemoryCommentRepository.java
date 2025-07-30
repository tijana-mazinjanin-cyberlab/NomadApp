package Repositories;

import model.Accommodation;
import model.Comment;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Repository
public class InMemoryCommentRepository implements IRepository<Comment, Long> {
    private ConcurrentMap<Long, Comment> comments = new ConcurrentHashMap<Long, Comment>();
    private static Long id = 0l;
    @Override
    public Collection<Comment> findAll() {
        return comments.values();
    }

    @Override
    public void create(Comment object) {
        this.comments.put(id, object);
        object.setId(id++);
    }

    @Override
    public Comment findOne(Long id) {
        return this.comments.get(id);
    }

    @Override
    public void update(Comment object) {
        this.comments.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.comments.remove(id);
    }
}
