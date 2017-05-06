<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/GetCinemaResult">
		<GetCinemaResult>
			<ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<cinemas>
				<xsl:for-each select="Cinemas/Cinema">
					<cinema>
						<code><xsl:value-of select="PlaceNo" /></code>
						<name><xsl:value-of select="PlaceName" /></name>
						<enabled>
							<xsl:choose>
									<xsl:when test="State='1'">true</xsl:when>
									<xsl:otherwise>false</xsl:otherwise>
							</xsl:choose>
						</enabled>
					</cinema>
				</xsl:for-each>
			</cinemas>
		</GetCinemaResult>
	</xsl:template>
</xsl:stylesheet>