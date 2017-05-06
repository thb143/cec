<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/payDetail">
		<data>
			<responseCode><xsl:value-of select="responseCode" /></responseCode>
			<responseMsg><xsl:value-of select="responseMsg" /></responseMsg>
			<memberCard>
				<balance><xsl:value-of select="accountBalance" /></balance>
				<score><xsl:value-of select="availableIntegral" /></score>
				<expirationTime><xsl:value-of select="validDate" /></expirationTime>
				<accLevelName><xsl:value-of select="policyName" /></accLevelName>
				<status>
					<xsl:choose>
						<xsl:when test="cardStatus = '正常'">0</xsl:when>
						<xsl:otherwise>1</xsl:otherwise>
					</xsl:choose>
				</status>
			</memberCard>
		</data>
	</xsl:template>
</xsl:stylesheet>