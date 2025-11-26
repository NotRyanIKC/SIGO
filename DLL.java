public class DLL<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public DLL() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Inserir no começo
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        size++;
    }

    // Inserir no fim
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
    }

    // Remover o primeiro elemento
    public void removeFirst() {
        if (head == null) return;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }

        size--;
    }

    // Remover o último elemento
    public void removeLast() {
        if (tail == null) return;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }

        size--;
    }

    // Remover um elemento específico
    public boolean remove(T data) {
        Node<T> current = head;

        while (current != null) {
            if (current.data.equals(data)) {

                if (current.prev != null) current.prev.next = current.next;
                else head = current.next;

                if (current.next != null) current.next.prev = current.prev;
                else tail = current.prev;

                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // Procurar elemento
    public T find(T data) {
        Node<T> current = head;

        while (current != null) {
            if (current.data.equals(data)) {
                return current.data;
            }
            current = current.next;
        }

        return null;
    }

    // Retorna o elemento no índice especificado
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora do intervalo: " + index);
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Implementa a interface Iterable
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    // Acessores importantes
    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Apenas para debug
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;

        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
    }
}
