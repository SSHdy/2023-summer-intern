package com.ryzw.housemanager.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>
 * jackson将Double序列化
 * </p>
 *
 * @author yz
 * @since 2019/5/9
 */
public class DoubleSerialize extends JsonSerializer<Float> {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public void serialize(Float aFloat, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (aFloat != null) {
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingSize(0);
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            jsonGenerator.writeString(decimalFormat.format(aFloat));
        }
    }
}
