package com.neogridTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            URL url = Main.class.getResource("input.txt");
            File inputFile = new File(url.getPath());

            FileReader fileReader = null;
            List<Instruction> list_of_instructions = new ArrayList<Instruction>();

            scanInputAndAddToList(inputFile, list_of_instructions);

            prepareDataAndPrintIt(list_of_instructions);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void prepareDataAndPrintIt(List<Instruction> list_of_instructions) {
        int work_start_hour_in_minutes = 540;
        int morning_duration = 180;
        int lunch_duration = 60;
        int time_counter_in_minutes;
        int afternoon_maximum_duration = 240;
        int work_minutes_total = 360;

        int number_of_assembly_lines = calculateNumberOfAssemblyLinesNeeded(list_of_instructions, work_minutes_total);

        for (int i = 0; i < number_of_assembly_lines; i++) {


            time_counter_in_minutes = work_start_hour_in_minutes;

            System.out.println("\nLinha de montagem " + (i + 1) + ": ");

            time_counter_in_minutes = prepareAndPrintMorning(list_of_instructions, morning_duration, time_counter_in_minutes);

            time_counter_in_minutes = prepareAndPrintLaunch(lunch_duration, time_counter_in_minutes, "Almoço");

            time_counter_in_minutes = prepareAndPrintAfternoon(list_of_instructions, time_counter_in_minutes, afternoon_maximum_duration);
            prepareAndPrintLaborGymnastics(time_counter_in_minutes, "Ginástica laboral");
        }

    }

    private static void prepareAndPrintLaborGymnastics(int time_counter_in_minutes, String s) {

        String time_string_format;
        time_string_format = transformTimeIntoString(time_counter_in_minutes);
        System.out.println(time_string_format + s);
    }

    private static int prepareAndPrintLaunch(int lunch_duration, int time_counter_in_minutes, String launch) {

        prepareAndPrintLaborGymnastics(time_counter_in_minutes, launch);
        time_counter_in_minutes += lunch_duration;
        return time_counter_in_minutes;
    }

    private static int prepareAndPrintAfternoon(List<Instruction> list_of_instructions, int time_counter_in_minutes, int afternoon_maximum_duration) {

        var afternoon_list = calculateAfternoon(list_of_instructions,
                afternoon_maximum_duration);

        for (int j = 0; j < afternoon_list.size(); j++) {

            time_counter_in_minutes = prepareAndPrintLaunch(afternoon_list.get(j).getMinutes(), time_counter_in_minutes, afternoon_list.get(j).toString());
        }
        return time_counter_in_minutes;
    }

    private static int prepareAndPrintMorning(List<Instruction> list_of_instructions, int morning_duration, int time_counter_in_minutes) {

        var morning_list = calculateMorning(list_of_instructions, morning_duration);

        for (int j = 0; j < morning_list.size(); j++) {

            time_counter_in_minutes = prepareAndPrintLaunch(morning_list.get(j).getMinutes(), time_counter_in_minutes, morning_list.get(j).toString());
        }
        return time_counter_in_minutes;
    }

    private static String transformTimeIntoString(int time_counter_in_minutes) {

        int hours;
        int minutes;

        hours = time_counter_in_minutes / 60;
        minutes = time_counter_in_minutes % 60;

        String time = String.format("%02d:%02d  ", hours, minutes);
        return time;
    }

    private static List<Instruction> calculateMorning(List<Instruction> list_of_instructions, int morning_duration) {

        List<Integer> list_of_index = new ArrayList<Integer>();
        List<Instruction> return_list = new ArrayList<Instruction>();

        for (int i = 0; i < list_of_instructions.size(); i++) {

            int target_duration = morning_duration;

            list_of_index.add(i);
            target_duration -= list_of_instructions.get(i).getMinutes();

            for (int j = i + 1; j < list_of_instructions.size(); j++) {
                if (target_duration < list_of_instructions.get(j).getMinutes()) {
                    continue;
                }
                if (target_duration > list_of_instructions.get(j).getMinutes()) {
                    target_duration -= list_of_instructions.get(j).getMinutes();
                    list_of_index.add(j);
                    continue;
                }
                if (target_duration == list_of_instructions.get(j).getMinutes()) {
                    target_duration -= list_of_instructions.get(j).getMinutes();
                    list_of_index.add(j);
                    break;
                }
            }
            if (target_duration == 0) {
                for (int k = 0; k < list_of_index.size(); k++) {
                    return_list.add(new Instruction(list_of_instructions.get(list_of_index.get(k) - k).getMinutes(),
                            list_of_instructions.get(list_of_index.get(k) - k).action));
                    list_of_instructions.remove(list_of_index.get(k) - k);

                }
                list_of_index.removeAll(list_of_index);
                return return_list;
            }
            list_of_index.removeAll(list_of_index);
            continue;
        }
        return return_list;
    }

    private static List<Instruction> calculateAfternoon(List<Instruction> list_of_instructions,
                                                        int afternoon_maximum_duration) {

        List<Integer> list_of_index = new ArrayList<Integer>();
        List<Instruction> return_list = new ArrayList<Instruction>();

        int minutes_accumulated = 0;

        for (int i = 0; i < list_of_instructions.size(); i++) {

            int target_duration_max = afternoon_maximum_duration;
            list_of_index.add(i);

            minutes_accumulated += list_of_instructions.get(i).getMinutes();

            for (int j = i + 1; j < list_of_instructions.size(); j++) {
                if ((minutes_accumulated + list_of_instructions.get(j).getMinutes()) > target_duration_max) {
                    continue;
                }
                minutes_accumulated += list_of_instructions.get(j).getMinutes();
                list_of_index.add(j);

            }
            if (!(list_of_index.size() == list_of_instructions.size())) {
                for (int value : list_of_index) {
                    return_list.add(new Instruction(list_of_instructions.get(value).getMinutes(),
                            list_of_instructions.get(value).action));
                    list_of_instructions.remove(value);

                }
                list_of_index.removeAll(list_of_index);
                return return_list;

            }
            for (int value : list_of_index) {

                return_list.add(new Instruction(list_of_instructions.get(value).getMinutes(),
                        list_of_instructions.get(value).action));

            }
            list_of_index.removeAll(list_of_index);
            list_of_instructions.removeAll(list_of_instructions);
            return return_list;

        }
        return return_list;
    }

    private static int calculateNumberOfAssemblyLinesNeeded(List<Instruction> list_of_instructions,
                                                            int work_minutes_total) {

        int total_minutes_in_input = 0;

        for (int i = 0; i < list_of_instructions.size(); i++) {
            total_minutes_in_input += list_of_instructions.get(i).getMinutes();
        }

        int number_of_assembly_lines = total_minutes_in_input / work_minutes_total;
        return number_of_assembly_lines;
    }

    private static void scanInputAndAddToList(File inputFile, List<Instruction> list_of_instructions)
            throws FileNotFoundException {

        var scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String line_without_maintenance = line.replaceAll("maintenance", "5min");
            String action_from_line = getActionFromLine(line_without_maintenance);
            int minutes_from_line = getMinutesFromLine(line_without_maintenance);

            list_of_instructions.add(new Instruction(minutes_from_line, action_from_line));
        }
        Collections.sort(list_of_instructions, Collections.reverseOrder());
        scanner.close();
    }

    private static int getMinutesFromLine(String line_without_maintenance) {

        String line_minutes_string = line_without_maintenance.replaceAll("[^0-9]", "");
        int line_minutes = Integer.parseInt(line_minutes_string);
        return line_minutes;
    }

    private static String getActionFromLine(String line_without_maintenance) {

        String line_without_number = line_without_maintenance.replaceAll("[^A-Za-z ()]", "");
        String line_action = line_without_number.replaceAll(" min", "");
        return line_action;
    }

}
