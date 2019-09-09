package com.ebran.todo.Todo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TodoService {
    private static int count = 3;
    private static List<Todo> items = new ArrayList<>();

    static {
        items.add(new Todo(1, "Wake Up!!!!!!!!!"));
        items.add(new Todo(1, "Take a Shower"));
        items.add(new Todo(1, "Eat Breakfast"));
        items.add(new Todo(1, "Learn Spring"));
    }

    public List<Todo> findAll() {
        return items;
    }

    public Todo findOneById(int id) {
        for (Todo todo : items) {
            if (todo.getId() == id)
                return todo;
        }
        return null;
    }

    public Todo addNewItem(Todo item) {
        if (item.getId() == null)
            item.setId(++count);
        items.add(item);
        return item;
    }

    public Todo deleteOneById(int id) {
        Iterator<Todo> iterator = items.iterator();

        while (iterator.hasNext()) {
            Todo item = iterator.next();
            if (item.getId() == id) {
                iterator.remove();
                return item;
            }
        }
        return null;
    }
}
