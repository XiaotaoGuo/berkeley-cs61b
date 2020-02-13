public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> result = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    public boolean isPalindrome(String word) {
        Deque d = wordToDeque(word);
        return isPalindromeHelper(d);
    }

    private boolean isPalindromeHelper(Deque<Character> d) {
        if (d.isEmpty() || d.size() == 1) {
            return true;
        }

        Character front = d.removeFirst();
        Character back = d.removeLast();
        if  (front != back) {
            return false;
        }
        return isPalindromeHelper(d);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        return isPalindromeOffByOneHelper(d, cc);
    }

    private boolean isPalindromeOffByOneHelper(Deque<Character> d, CharacterComparator cc) {
        if (d.isEmpty() || d.size() == 1) {
            return true;
        }

        Character front = d.removeFirst();
        Character back = d.removeLast();
        if (!cc.equalChars(front, back)) {
            return false;
        }
        return isPalindromeOffByOneHelper(d, cc);
    }
}
