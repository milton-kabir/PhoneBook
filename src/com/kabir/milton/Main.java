//package phonebook;
package com.kabir.milton;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

class Phones {
    private final List<Phone> phones;
    private boolean sorted = false;
    private long sortTimeLimit;

    Comparator<Phone> defaultComparator = Comparator.comparing(Phone::getName);

    public long getSortTimeLimit() {
        return sortTimeLimit;
    }

    public void setSortTimeLimit(long sortTimeLimit) {
        this.sortTimeLimit = sortTimeLimit;
    }

    public boolean isSorted() {
        return sorted;
    }

    public Phones(List<Phone> phones) {
        this.phones = phones;
    }

    public Phone linearSearch(Phone value, Comparator<Phone> comparator) {
        for (Phone phone : phones) {
            if (comparator.compare(value, phone) == 0) {
                return phone;
            }
        }
        return null;
    }

    public Phones linearSearch(List<Phone> names, Comparator<Phone> comparator) {
        return new Phones(names.stream().map(item -> linearSearch(item, comparator)).collect(Collectors.toList()));
    }

    public Phones linearSearch(List<Phone> names) {
        return linearSearch(names, defaultComparator);
    }

    public Phone jumpSearch(Phone value, Comparator<Phone> comparator) {
        if (!isSorted()) {
            sortBubble();
        }
        int period = (int) Math.sqrt(phones.size());
        int previouslyIndex = 0;
        int currentIndex = 0;
        if (comparator.compare(phones.get(0), value) > 0
                || comparator.compare(phones.get(phones.size() - 1), value) < 0) {
            return null;
        }
        while (currentIndex < phones.size()) {
            int condition = comparator.compare(phones.get(currentIndex), value);
            if (condition == 0) {
                break;
            } else if (condition < 0) {
                previouslyIndex = currentIndex;
                currentIndex = Math.min(currentIndex + period, phones.size() - 1);
            } else {
                for (currentIndex--; currentIndex > previouslyIndex; currentIndex--) {
                    condition = comparator.compare(phones.get(currentIndex), value);
                    if (condition < 0) return null;
                    else if (condition == 0) break;
                }
            }
        }
        return phones.get(currentIndex);
    }

    public Phones jumpSearch(List<Phone> names, Comparator<Phone> comparator) {
        return new Phones(names.stream().map(item -> jumpSearch(item, comparator)).collect(Collectors.toList()));
    }

    public Phones jumpSearch(List<Phone> names) {
        return jumpSearch(names, defaultComparator);
    }

    public int size() {
        return phones.size();
    }

    public static Phones loadPhones(Path path) throws IOException {
        return new Phones(Files.lines(path, StandardCharsets.UTF_8)
                .map(line -> {
                    String[] words = line.split("\\s+", 2);
                    return new Phone(words[1]);
                }).collect(Collectors.toList()));
    }

    public static <T> void swap(List<T> array, int firstIndex, int secondIndex) {
        T temp = array.get(firstIndex);
        array.set(firstIndex, array.get(secondIndex));
        array.set(secondIndex, temp);
    }

    public void sortBubble(Comparator<Phone> comparator) {
        long finish = System.currentTimeMillis() + getSortTimeLimit();
        boolean replace = true;

        for (int i = 0; i < phones.size() - 1 && replace; i++) {
            replace = false;
            for (int j = 0; j < phones.size() - i - 1; j++) {
                if (comparator.compare(phones.get(j), phones.get(j + 1)) > 0) {
                    swap(this.phones, j, j + 1);
                    replace = true;
                }
                if (finish <= System.currentTimeMillis()) {
                    throw new IllegalStateException("time out");
                }
            }
        }
        sorted = true;
    }

    public void sortBubble() {
        sortBubble(defaultComparator);
    }

    public void quickSort(Comparator<Phone> comparator) {
        quickSort(this.phones, 0, this.size() - 1, comparator);
    }

    public void quickSort() {
        quickSort(defaultComparator);
    }

    private static <T> void quickSort(List<T> array, int leftIndex, int rightIndex, Comparator<T> comparator) {
        if (leftIndex < rightIndex) {
            int pivotIndex = partition(array, leftIndex, rightIndex, comparator); // the pivot is already on its place
            quickSort(array, leftIndex, pivotIndex - 1, comparator);  // sort the left subarray
            quickSort(array, pivotIndex + 1, rightIndex, comparator); // sort the right subarray
        }
    }

    private static <T> int partition(List<T> array, int leftIndex, int rightIndex, Comparator<T> comparator) {
        T pivot = array.get(rightIndex);  // choose the rightmost element as the pivot
        int partitionIndex = leftIndex; // the first element greater than the pivot
        /* move large values into the right side of the array */
        for (int i = leftIndex; i < rightIndex; i++) {
            if (comparator.compare(array.get(i), pivot) <= 0) { // may be used '<' as well
                swap(array, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(array, partitionIndex, rightIndex); // put the pivot on a suitable position
        return partitionIndex;
    }

    private static <T> T quickSearch(List<T> array, int leftIndex, int rightIndex, T searchValue, Comparator<T> comparator) {
        if (leftIndex > rightIndex) return null;
        int middleIndex = (leftIndex + rightIndex) >>> 1;
        int comparison = comparator.compare(array.get(middleIndex), searchValue);
        if (comparison == 0) return array.get(middleIndex);
        if (comparison < 0) return quickSearch(array, leftIndex, middleIndex - 1, searchValue, comparator);
        else return quickSearch(array, middleIndex + 1, rightIndex, searchValue, comparator);
    }

    public Phone quickSearch(Phone phone, Comparator<Phone> comparator) {
        return quickSearch(this.phones, 0, this.size() - 1, phone, comparator);
    }

    public Phones quickSearch(List<Phone> names, Comparator<Phone> comparator) {
        return new Phones(names.stream().map(item -> quickSearch(item, comparator)).collect(Collectors.toList()));
    }

    public Phones quickSearch(List<Phone> names) {
        return quickSearch(names, defaultComparator);
    }

    public void prepareMap() {
        Map<String, Phone> index = new HashMap<>();
        for (Phone phone : this.phones) {
            index.put(phone.getName(), phone);
        }
    }

}

class Phone {
    private final String name;

    public Phone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

public class Main {
    public static String getTime(Duration duration) {
        return String.format("%d min. %d sec. %d ms.", duration.toMinutes(), duration.toSeconds() % 60, duration.toMillis() % 1000);
    }

    public static void main(String[] args) throws IOException {
        Phones phones = Phones.loadPhones(Paths.get("C:\\Users\\drifter\\Downloads\\directory.txt"));
        List<String> names = Files.lines(Paths.get("C:\\Users\\drifter\\Downloads\\find.txt"), StandardCharsets.UTF_8)
                .collect(Collectors.toList());
        List<Phone> searchList = names.stream().map(Phone::new).collect(Collectors.toList());

        System.out.println("Start searching (linear search)...");
        long start = System.currentTimeMillis();
        Phones filtered = phones.linearSearch(searchList);
        Duration searchDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        phones.setSortTimeLimit(searchDuration.toMillis() * 10);
        System.out.printf("Found %d / %d entries. Time taken:  %s\n\n"
                , filtered.size()
                , names.size()
                , getTime(searchDuration)
        );

        System.out.println("Start searching (bubble sort + jump search)...");
        Duration sortDuration;
        try {
            start = System.currentTimeMillis();
            phones.sortBubble();
            sortDuration = Duration.ofMillis(System.currentTimeMillis() - start);

            start = System.currentTimeMillis();
            filtered = phones.jumpSearch(searchList);
            searchDuration = Duration.ofMillis(System.currentTimeMillis() - start);
            Duration searchAndSortDuration = sortDuration.plus(searchDuration);

            System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                    , filtered.size()
                    , names.size()
                    , getTime(searchAndSortDuration)
            );
            System.out.printf("Sorting time: %s\n", getTime(sortDuration));
            System.out.printf("Searching time: %s\n\n", getTime(sortDuration));
        } catch (IllegalStateException error) {
            sortDuration = Duration.ofMillis(System.currentTimeMillis() - start);
            start = System.currentTimeMillis();
            filtered = phones.linearSearch(searchList);
            searchDuration = Duration.ofMillis(System.currentTimeMillis() - start);
            Duration searchAndSortDuration = sortDuration.plus(searchDuration);
            System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                    , filtered.size()
                    , names.size()
                    , getTime(searchAndSortDuration)
            );
            System.out.printf("Sorting time: %s - STOPPED, moved to linear search\n", getTime(sortDuration));
            System.out.printf("Searching time: %s\n\n", getTime(searchDuration));

        }

        System.out.println("Start searching (quick sort + binary search)...");
        phones = Phones.loadPhones(Paths.get("C:\\Users\\drifter\\Downloads\\directory.txt"));
        start = System.currentTimeMillis();
        phones.quickSort();
        sortDuration = Duration.ofMillis(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        filtered = phones.quickSearch(searchList);
        searchDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        Duration searchAndSortDuration = sortDuration.plus(searchDuration);

        System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                , filtered.size()
                , names.size()
                , getTime(searchAndSortDuration)
        );
        System.out.printf("Sorting time: %s\n", getTime(sortDuration));
        System.out.printf("Searching time: %s\n\n", getTime(searchDuration));


        System.out.println("Start searching (hash table)...");
        phones = Phones.loadPhones(Paths.get("C:\\Users\\drifter\\Downloads\\directory.txt"));
        start = System.currentTimeMillis();
        phones.prepareMap();
        sortDuration = Duration.ofMillis(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        filtered = phones.quickSearch(searchList);
        searchDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        searchAndSortDuration = sortDuration.plus(searchDuration);

        System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                , filtered.size()
                , names.size()
                , getTime(searchAndSortDuration)
        );
        System.out.printf("Creating time: %s\n", getTime(sortDuration));
        System.out.printf("Searching time: %s\n\n", getTime(searchDuration));
    }
}