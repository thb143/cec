<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data/card">
		<memberCard>
			<balance><xsl:value-of select="balance" /></balance>
			<score><xsl:value-of select="cumulationMarking" /></score>
			<accLevelName><xsl:value-of select="gradeDesc" /></accLevelName>
			<expirationTime><xsl:value-of select="invalidationDate" /></expirationTime>
			<status>
				<xsl:choose>
					<xsl:when test="avaiFlg = 'Y'">0</xsl:when>
					<xsl:when test="avaiFlg = 'N'">1</xsl:when>
				</xsl:choose>
			</status>
		</memberCard>
	</xsl:template>
</xsl:stylesheet>