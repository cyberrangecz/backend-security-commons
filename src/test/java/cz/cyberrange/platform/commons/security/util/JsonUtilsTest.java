package cz.cyberrange.platform.commons.security.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  private static JsonObject parse(String json) {
    return JsonParser.parseString(json).getAsJsonObject();
  }

  @Test
  @DisplayName("getAsString_primitiveMember_returnsValue")
  void getAsString_primitiveMember_returnsValue() {
    assertThat(JsonUtils.getAsString(parse("{\"name\":\"alice\"}"), "name")).isEqualTo("alice");
  }

  @Test
  @DisplayName("getAsString_missingMember_returnsNull")
  void getAsString_missingMember_returnsNull() {
    assertThat(JsonUtils.getAsString(parse("{}"), "name")).isNull();
  }

  @Test
  @DisplayName("getAsString_nonPrimitiveMember_returnsNull")
  void getAsString_nonPrimitiveMember_returnsNull() {
    assertThat(JsonUtils.getAsString(parse("{\"name\":{\"a\":1}}"), "name")).isNull();
  }

  @Test
  @DisplayName("getAsBoolean_primitiveMember_returnsValue")
  void getAsBoolean_primitiveMember_returnsValue() {
    assertThat(JsonUtils.getAsBoolean(parse("{\"flag\":true}"), "flag")).isTrue();
  }

  @Test
  @DisplayName("getAsBoolean_missingMember_returnsNull")
  void getAsBoolean_missingMember_returnsNull() {
    assertThat(JsonUtils.getAsBoolean(parse("{}"), "flag")).isNull();
  }

  @Test
  @DisplayName("getAsStringList_arrayMember_returnsAllElements")
  void getAsStringList_arrayMember_returnsAllElements() {
    assertThat(JsonUtils.getAsStringList(parse("{\"scopes\":[\"a\",\"b\"]}"), "scopes"))
        .containsExactly("a", "b");
  }

  @Test
  @DisplayName("getAsStringList_scalarMember_returnsSingleElementList")
  void getAsStringList_scalarMember_returnsSingleElementList() {
    assertThat(JsonUtils.getAsStringList(parse("{\"scopes\":\"a\"}"), "scopes"))
        .containsExactly("a");
  }

  @Test
  @DisplayName("getAsStringList_missingMember_returnsNull")
  void getAsStringList_missingMember_returnsNull() {
    assertThat(JsonUtils.getAsStringList(parse("{}"), "scopes")).isNull();
  }

  @Test
  @DisplayName("getAsJwsAlgorithmList_arrayMember_returnsParsedAlgorithms")
  void getAsJwsAlgorithmList_arrayMember_returnsParsedAlgorithms() {
    assertThat(JsonUtils.getAsJwsAlgorithmList(parse("{\"algs\":[\"RS256\"]}"), "algs"))
        .containsExactly(JWSAlgorithm.RS256);
  }

  @Test
  @DisplayName("getAsJweAlgorithmList_arrayMember_returnsParsedAlgorithms")
  void getAsJweAlgorithmList_arrayMember_returnsParsedAlgorithms() {
    assertThat(JsonUtils.getAsJweAlgorithmList(parse("{\"algs\":[\"RSA-OAEP-256\"]}"), "algs"))
        .containsExactly(JWEAlgorithm.RSA_OAEP_256);
  }

  @Test
  @DisplayName("getAsEncryptionMethodList_arrayMember_returnsParsedMethods")
  void getAsEncryptionMethodList_arrayMember_returnsParsedMethods() {
    assertThat(JsonUtils.getAsEncryptionMethodList(parse("{\"encs\":[\"A128GCM\"]}"), "encs"))
        .containsExactly(EncryptionMethod.A128GCM);
  }

  @Test
  @DisplayName("getAsJwsAlgorithmList_missingMember_returnsNull")
  void getAsJwsAlgorithmList_missingMember_returnsNull() {
    assertThat(JsonUtils.getAsJwsAlgorithmList(parse("{}"), "algs")).isNull();
    assertThat(JsonUtils.getAsJweAlgorithmList(parse("{}"), "algs")).isNull();
    assertThat(JsonUtils.getAsEncryptionMethodList(parse("{}"), "encs")).isNull();
  }
}
