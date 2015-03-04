package uk.co.spookypeanut.lovetag;

public class Tag implements Comparable {
    String mName;
    boolean mActive;
    boolean mPresent;
    public Tag(String name) {
        mName = name.toLowerCase();
        mActive = false;
        mPresent = false;
    }
    public String toString() {
        return mName;
    }
    public int compareTo(Object other) {
        if (!(other instanceof Tag)) {
            throw new ClassCastException("Comparing invalid items");
        }
        Tag other_tag = (Tag) other;
        // If this one is active and the other isn't, it's less
        // ie higher up in list
        if (mActive && !other_tag.mActive) {
            return -1;
        }
        if (!mActive && other_tag.mActive) {
            return 1;
        }
        // Don't sort by name, to try to keep existing pattern
        return 0;
    }

    public boolean equals(Object other) {
        // Note that .equals() and .compareTo() seem incompatible,
        // but it's all dependent on your point of view. This is used to make
        // .contains() on a TagList work
        if (!(other instanceof Tag)) {
            throw new ClassCastException("Comparing invalid items");
        }
        Tag other_tag = (Tag) other;
        return mName.equals(other_tag.mName);
    }

}