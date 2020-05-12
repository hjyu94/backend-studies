package me.hjeong._4_environment;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component @Profile("release")
public class ClassForRelease {
}
