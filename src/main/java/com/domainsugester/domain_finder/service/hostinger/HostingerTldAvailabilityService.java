package com.domainsugester.domain_finder.service.hostinger;

import com.domainsugester.domain_finder.client.HostingerClient;
import com.domainsugester.domain_finder.dto.external.Hostinger.HostingerTldAvailabilityRequest;
import com.domainsugester.domain_finder.dto.external.Hostinger.HostingerTldAvaliabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostingerTldAvailabilityService {
    private final HostingerClient hostingerClient;
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int DOMAIN_LENGTH = 16;

    public Set<String> getAvaliableTldSet(Set<String> tldSet) {
        tldSet = filterTlds(tldSet);
        int batchSize = (tldSet.size() + 2) / 3;
        var batches = splitIntoBatches(tldSet, batchSize);
        Set<String> avaliableTlds = new HashSet<>();
        for(int i =0; i < 3; i++){
            Set<HostingerTldAvaliabilityResponse> responses = executeRequestBatch(batches, generateDomain());
            avaliableTlds.addAll(extractAvailableTldSet(responses));
        }
        return avaliableTlds;
    }

    private Set<String> filterTlds(Set<String> tldSet){
        return  tldSet.stream()
                .map(tld -> tld.substring(9))
                .filter(tld -> !tld.startsWith("xn--"))
                .collect(Collectors.toSet());
    }
    private Set<HostingerTldAvaliabilityResponse> executeRequestBatch(List<Set<String>> subSets, String domain){
        List<Set<HostingerTldAvaliabilityResponse>> responseBatches = subSets.parallelStream()
                .map(subSet -> {
                    HostingerTldAvailabilityRequest request = new HostingerTldAvailabilityRequest(
                            domain,
                            subSet,
                            false
                    );
                    System.out.println("Request: " + request);

                    var a = hostingerClient.getHostingerTldAvaliabilities(request);
                    System.out.println(a);

                    return hostingerClient.getHostingerTldAvaliabilities(request);
                })
                .toList();
        return responseBatches.stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
    private List<Set<String>> splitIntoBatches(Set<String> tldSet, int batchSize) {

        List<String> tlds = new ArrayList<>(tldSet);
        List<Set<String>> batches = new ArrayList<>();

        for (int i = 0; i < tlds.size(); i += batchSize) {

            batches.add(
                    new HashSet<>(
                            tlds.subList(
                                    i,
                                    Math.min(i + batchSize, tlds.size())
                            )
                    )
            );
        }
        return batches;
    }

    private String generateDomain(){
        StringBuilder domain = new StringBuilder();
        for (int i = 0; i < DOMAIN_LENGTH; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(LETTERS.length());
            domain.append(LETTERS.charAt(randomIndex));
        }
        return domain.toString();
    }
    private Set<String> extractAvailableTldSet(Set<HostingerTldAvaliabilityResponse> responses){
        return responses.stream()
                .filter(HostingerTldAvaliabilityResponse::isAvailable)
                .map(avaliable -> {
                    String domain = avaliable.domain();
                    return domain.substring(domain.indexOf('.') + 1);
                })
                .collect(Collectors.toSet());
    }
}
