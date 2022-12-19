package bsu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class MedianTemperature {
    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, DoubleWritable> {
        private final Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                String country = tokenizer.nextToken();
                String temp = tokenizer.nextToken();
                word.set(country);
                context.write(word, new DoubleWritable(Double.parseDouble(temp)));
            }
        }
    }

    public static class MedianReducer
            extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        public void reduce(Text key, Iterable<DoubleWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            List<Double> temps = new ArrayList<>();
            values.forEach(el -> temps.add(el.get()));
            temps.sort(Comparator.comparingDouble(Double::doubleValue));
            int k = temps.size() / 2;
            DoubleWritable result = new DoubleWritable();
            if (temps.size() % 2 == 0) {
                double median = (temps.get(k - 1) + temps.get(k)) / 2.;
                result.set(median);
            } else {
                result.set(temps.get(k));
            }
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: MedianTemperature <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "Median temperature");
        job.setJarByClass(MedianTemperature.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(MedianReducer.class);
        job.setReducerClass(MedianReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job,
                new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
