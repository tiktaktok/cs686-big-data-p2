package edu.usfca.cs.mr.wind_farm;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static edu.usfca.cs.mr.util.Feature.*;

public class WindFarmMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(),
                GEOHASH,
                TEMPERATURE_SURFACE,
                U_COMPONENT_OF_WIND_MAXIMUM_WIND, V_COMPONENT_OF_WIND_MAXIMUM_WIND,
                SNOW_DEPTH_SURFACE);

        String geohash = observation.getGeohash().substring(0, 3);

        float temperature = observation.getFeature(TEMPERATURE_SURFACE, Float.class);

        double u = observation.getFeature(U_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        double v = observation.getFeature(V_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        float windSpeed = (float) Math.sqrt(u * u + v * v);

        float snowDepth = observation.getFeature(SNOW_DEPTH_SURFACE, Float.class);

        context.write(new Text(geohash),
                new Text("" + temperature + ':' + windSpeed + ':' + snowDepth));
    }
}
