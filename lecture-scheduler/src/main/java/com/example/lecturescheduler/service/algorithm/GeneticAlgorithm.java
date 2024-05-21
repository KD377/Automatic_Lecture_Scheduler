package com.example.lecturescheduler.service.algorithm;

import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.model.SingleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;



@Service
public class GeneticAlgorithm {

    private final PopulationGenerator populationGenerator;

    private static final int POPULATION_SIZE = 10;

    private static final int MAX_GENERATIONS = 40;

    private static final double MUTATION_RATE = 0.5;

    private static final double CROSSOVER_RATE = 0.8;

    private static final int TOURNAMENT_SIZE = 5;
    private final Random random = new Random();

    @Autowired
    public GeneticAlgorithm(PopulationGenerator populationGenerator) {
        this.populationGenerator = populationGenerator;
    }

    public Chromosome run() {
        List<Chromosome> population = initializePopulation();
        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            evaluatePopulation(population);
            List<Chromosome> newPopulation = new ArrayList<>();

            while (newPopulation.size() < POPULATION_SIZE) {
                Chromosome parent1 = selectParentTournament(population);
                Chromosome parent2 = selectParentTournament(population);

                Chromosome offspring1, offspring2;

                if (Math.random() < CROSSOVER_RATE) {
                    List<Chromosome> offspringPair = crossover(parent1, parent2);
                    offspring1 = offspringPair.get(0);
                    offspring2 = offspringPair.get(1);
                } else {
                    offspring1 = new Chromosome(new ArrayList<>(parent1.getLectureSessions()));
                    offspring2 = new Chromosome(new ArrayList<>(parent2.getLectureSessions()));
                }

                if (Math.random() < MUTATION_RATE) {
                    mutate(offspring1);
                }

                if (Math.random() < MUTATION_RATE) {
                    mutate(offspring2);
                }

                newPopulation.add(offspring1);
                newPopulation.add(offspring2);
            }

            population = newPopulation;
        }

        evaluatePopulation(population);
        return getBestChromosome(population);
    }

    public Chromosome getBestChromosome(List<Chromosome> population) {
        Chromosome bestChromosome = null;
        double highestFitnessScore = Double.NEGATIVE_INFINITY;

        for (Chromosome chromosome : population) {
            if (chromosome.getFitnessScore() > highestFitnessScore) {
                highestFitnessScore = chromosome.getFitnessScore();
                bestChromosome = chromosome;
            }
        }

        return bestChromosome;
    }

    private void evaluatePopulation(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            int clashes = calculateClashes(chromosome);
            int breaks = calculateBreaks(chromosome);
            int notMetPreferences = calculatePreferences(chromosome);
            double fitness = 1.0 / ((clashes * 100) + (breaks) + notMetPreferences + 1);
            chromosome.setFitnessScore(fitness);
        }
    }

    private int calculatePreferences(Chromosome chromosome) {
        int notMetPreferences = 0;
        for(LectureSession session : chromosome.getLectureSessions()) {
            int dayOfWeekNumber = session.getDay().getValue();
            List<Boolean> preferences = session.getInstructor().getPreferences();
            if(!preferences.get(dayOfWeekNumber - 1)){
                notMetPreferences++;
            }
        }
        return notMetPreferences;
    }


    private int calculateBreaks(Chromosome chromosome) {
        Map<SingleGroup, Map<DayOfWeek, List<Integer>>> groupSchedule = new HashMap<>();

        for (LectureSession session : chromosome.getLectureSessions()) {
            groupSchedule
                    .computeIfAbsent(session.getGroup(), k -> new HashMap<>())
                    .computeIfAbsent(session.getDay(), k -> new ArrayList<>())
                    .add(session.getNumberOfTimeSlot());
        }

        int totalBreaks = 0;
        for (Map<DayOfWeek, List<Integer>> daySchedule : groupSchedule.values()) {
            for (List<Integer> timeSlots : daySchedule.values()) {
                Collections.sort(timeSlots);
                for (int i = 1; i < timeSlots.size(); i++) {
                    totalBreaks += timeSlots.get(i) - timeSlots.get(i - 1) - 1;
                }
            }
        }

        return totalBreaks;
    }

    private int calculateClashes(Chromosome chromosome) {
        List<LectureSession> sessions = chromosome.getLectureSessions();
        int conflicts = 0;

        for (int i = 0; i < sessions.size(); i++) {
            LectureSession session = sessions.get(i);
            for (int j = i + 1; j < sessions.size(); j++) {
                LectureSession otherSession = sessions.get(j);
                if (!checkConflicts(session, otherSession)) {
                    conflicts++;
                    break;
                }
            }
        }

        return conflicts;
    }

    private void mutate(Chromosome chromosome) {
        List<LectureSession> sessions = chromosome.getLectureSessions();
        int sessionIndex = random.nextInt(sessions.size());
        LectureSession sessionToMutate = sessions.get(sessionIndex);

        LectureSession newSession = generateRandomValidSession(sessionToMutate);
        int retryCount = 0;
        int maxRetries = 100;

        while (!isValidMutation(newSession, sessions) && retryCount < maxRetries) {
            newSession = generateRandomValidSession(sessionToMutate);
            retryCount++;
        }

        if (retryCount < maxRetries) {
            sessions.set(sessionIndex, newSession);
        } else {
            System.err.println("Failed to generate a valid mutation after " + maxRetries + " retries.");
        }
    }

    private boolean isValidMutation(LectureSession newSession, List<LectureSession> sessions) {
        for (LectureSession existingSession : sessions) {
            if (!checkConflicts(newSession, existingSession)) {
                return false;
            }
        }
        return true;
    }

    private LectureSession generateRandomValidSession(LectureSession sessionToMutate) {

        LectureSession newSession = new LectureSession(sessionToMutate);
        newSession.setDay(drawRandomDay());
        newSession.setNumberOfTimeSlot(random.nextInt(5) + 1);
        return newSession;
    }

    private DayOfWeek drawRandomDay() {
        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        switch (randomNumber) {
            case 1:
                return DayOfWeek.MONDAY;
            case 2:
                return DayOfWeek.TUESDAY;
            case 3:
                return DayOfWeek.WEDNESDAY;
            case 4:
                return DayOfWeek.THURSDAY;
            case 5:
                return DayOfWeek.FRIDAY;
            default:
                throw new IllegalArgumentException("Invalid random number: " + randomNumber);
        }
    }


    private boolean checkConflicts(LectureSession newSession,LectureSession existingSession) {
        boolean sameTime = existingSession.getDay() == newSession.getDay() &&
                existingSession.getNumberOfTimeSlot() == newSession.getNumberOfTimeSlot();
        if (sameTime) {
            if (existingSession.getGroup().equals(newSession.getGroup())) {
                return false;
            }
            if (existingSession.getInstructor().equals(newSession.getInstructor())) {
                return false;
            }
            if (existingSession.getClassroom().equals(newSession.getClassroom())) {
                return false;
            }
        }

        return true;
    }

    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(populationGenerator.generateChromosome());
        }
        return population;
    }

    private Chromosome selectParentTournament(List<Chromosome> population) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        Chromosome best = tournament.getFirst();
        for (Chromosome chromosome : tournament) {
            if (chromosome.getFitnessScore() > best.getFitnessScore()) {
                best = chromosome;
            }
        }
        return best;
    }

    private List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
        int size = parent1.getLectureSessions().size();
        List<LectureSession> offspring1Sessions = new ArrayList<>();
        List<LectureSession> offspring2Sessions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (random.nextBoolean()) {
                offspring1Sessions.add(parent1.getLectureSessions().get(i));
            } else {
                offspring1Sessions.add(parent2.getLectureSessions().get(i));
            }

            if (random.nextBoolean()) {
                offspring2Sessions.add(parent1.getLectureSessions().get(i));
            } else {
                offspring2Sessions.add(parent2.getLectureSessions().get(i));
            }
        }

        Chromosome offspring1 = new Chromosome(offspring1Sessions);
        Chromosome offspring2 = new Chromosome(offspring2Sessions);

        return Arrays.asList(offspring1, offspring2);
    }





}
