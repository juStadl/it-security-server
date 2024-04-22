package de.dhbw.itsecurity.itsecurityserver.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class Authfilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        X509Certificate[] certs = (X509Certificate[]) request
                .getAttribute("jakarta.servlet.request.X509Certificate");
        if(certs != null && certs.length > 0){
            System.out.println("X.509 client authentification certificate: " + certs[0]);
        }
        String dn = certs[0].getSubjectX500Principal().getName();
        System.out.println(gatherSubjectMap(dn).get("CN"));

        doFilter(request, response, filterChain);
    }

    public Map<String, String> gatherSubjectMap(String dn){
        Map<String, String> dnMap = new HashMap<>();
        if(dn != null){
            dnMap = Arrays.stream(dn.split(",")).sequential().map(item -> item.split("="))
                    .collect(Collectors.toMap(part -> part[0], part -> part[1]));
        }
        return dnMap;
    }
}
