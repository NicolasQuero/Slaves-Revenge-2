package utilities;

public class Test {
	@SuppressWarnings("unchecked")
	public Authentication getAuthentication(HttpServletRequest request) throws ParseException, BadJOSEException, JOSEException {
	    String idToken = request.getHeader("Authorization");
	    if (null == idToken) {
	        //          throw new CognitoException(NO_TOKEN_FOUND,
	        //                  CognitoException.NO_TOKEN_PROVIDED_EXCEPTION, 
	        //                  "No token found in Http Authorization Header");
	        System.out.println("No token found in Http Authorization Header");
	    } else {
	        idToken = extractAndDecodeJwt(idToken);
	        JWTClaimsSet claimsSet = null;
	        claimsSet = configurableJWTProcessor.process(idToken, null);
	        if (!isIssuedCorrectly(claimsSet)) {
	            //                  throw new CognitoException(INVALID_TOKEN,
	            //                          CognitoException.INVALID_TOKEN_EXCEPTION_CODE, 
	            //                          String.format("Issuer %s in JWT token doesn't match cognito idp %s", 
	            //                                  claimsSet.getIssuer(),jwtConfiguration.getCognitoIdentityPoolUrl()));
	            System.out.println("Issuer in JWT token doesn't match cognito idp");
	        }

	        if(!isIdToken(claimsSet)) {
	            //                  throw new CognitoException(INVALID_TOKEN,
	            //                          CognitoException.NOT_A_TOKEN_EXCEPTION, 
	            //                          "JWT Token doesn't seem to be an ID Token");
	            System.out.println("JWT Token doesn't seem to be an ID Token");
	        }

	        String username = claimsSet.getClaims()
	                .get("cognito:username").toString();

	        @SuppressWarnings("unchecked")
	        List<String> groups = (List<String>) claimsSet.getClaims()
	        .get("cognito:groups");
	        List<String> grantedAuthorities = convertList(groups, group-> new 
	                SimpleGrantedAuthority("ROLE_" + group.toUpperCase()));
	        User user = new User(username, "", grantedAuthorities);

	        return new CognitoJwtAuthentication(user, claimsSet, grantedAuthorities); // error here
	    }
	}
}
